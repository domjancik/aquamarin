/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.StockDAO;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class StockDAODerby implements StockDAO {

    private static StockDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement checkPs;
    private PreparedStatement addPs;
    private PreparedStatement addMinPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    private PreparedStatement setMinPs;
    private PreparedStatement setBlockedPs;
    
    private Connection connection;
    
    private StockDAODerby(Connection connection) {
        this.connection = connection;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM STOCK WHERE client_id = ?");
            findOnePs = connection.prepareStatement("SELECT * FROM STOCK WHERE client_id = ? AND product_id = ?");
            checkPs = connection.prepareStatement("SELECT amount FROM STOCK WHERE client_id = ? AND product_id = ?");
            addPs = connection.prepareStatement("INSERT INTO STOCK VALUES (?, ?, ?, DEFAULT, DEFAULT)");
            addMinPs = connection.prepareStatement("INSERT INTO STOCK VALUES (?, ?, 0, ?, DEFAULT)");
            deletePs = connection.prepareStatement("DELETE FROM STOCK WHERE client_id = ? AND product_id = ?");
            updatePs = connection.prepareStatement("UPDATE STOCK SET amount = ? WHERE client_id = ? AND product_id = ?");
            setMinPs = connection.prepareStatement("UPDATE STOCK SET amount_min = ? WHERE client_id = ? AND product_id = ?");
            setBlockedPs = connection.prepareStatement("UPDATE STOCK SET amount_blocked = ? WHERE client_id = ? AND product_id = ?");
        } catch (SQLException ex) {
            Logger.getLogger(StockDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static StockDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new StockDAODerby(connection);
        }
        return instance;
    }

    @Override
    public Stock find(Client c) {
        try {
            findPs.setInt(1, c.getId().getId());
            ResultSet rs = findPs.executeQuery();
            Set<StockItem> items = new HashSet<StockItem>();
            while (rs.next()) {
                Product p = ProductDAODerby.getInstance(connection).find(new ProductId(rs.getInt("product_id")));
                
                if (items.add(new StockItem(p,
                        rs.getInt("amount"),
                        rs.getInt("amount_min"),
                        rs.getInt("amount_blocked"))) == false) {
                    Logger.getLogger(StockDAODerby.class.getName()).log(Level.WARNING, "Duplicate product entry in stock");
                }
            }
            rs.close();
            return new Stock(c, items);
        } catch (SQLException ex) {
            Logger.getLogger(StockDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void alter(int clientId, int productId, int amount) throws SQLException {
        checkPs.setInt(1, clientId);
        checkPs.setInt(2, productId);
        ResultSet rs = checkPs.executeQuery();
        if (rs.next()) {
            int curAmount = rs.getInt("amount");
            updatePs.setInt(1, curAmount + amount);
            updatePs.setInt(2, clientId);
            updatePs.setInt(3, productId);
            updatePs.executeUpdate();
        } else {
            addPs.setInt(1, clientId);
            addPs.setInt(2, productId);
            addPs.setInt(3, amount);
            addPs.executeUpdate();
        }
        rs.close();
    }

    @Override
    public void alterItem(TrackedProduct item) throws AquamarinException {
        try {
            findOnePs.setInt(1, item.getSourceStock().getId().getId());
            findOnePs.setInt(2, item.getProduct().getId().getId());
            ResultSet rs = findOnePs.executeQuery();
            int amount;
            if (rs.next()) {
                amount = rs.getInt("amount");
            } else {
                throw new AquamarinException("Altering a missing item");
            }
            amount += item.getAmount();
            if (amount < 0)
                throw new OutOfBoundsException("Trying to reduce item amount under 0");
            updatePs.setInt(1, amount);
            updatePs.setInt(2, item.getSourceStock().getId().getId());
            updatePs.setInt(3, item.getProduct().getId().getId());
            updatePs.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        }
    }
    
    @Override
    public boolean alterItems(Stock target, Collection<CountedProduct> items) {
        try {
            connection.setAutoCommit(false);
            
            int clientId = target.getOwner().getId().getId();
            for (CountedProduct stockItem : items) {
                int productId = stockItem.getProduct().getId().getId();
                alter(clientId, productId, stockItem.getAmount());
            }
            connection.commit();
            
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean transferItems(Stock source, Stock target, Collection<CountedProduct> items) {
        try {
            connection.setAutoCommit(false);
            
            int clientIdSource = source.getOwner().getId().getId();
            int clientIdTarget = target.getOwner().getId().getId(); 
            for (CountedProduct stockItem : items) {
                int productId = stockItem.getProduct().getId().getId();
                alter(clientIdSource, productId, -stockItem.getAmount());
                alter(clientIdTarget, productId, stockItem.getAmount());
            }
            connection.commit();
            
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(StockDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean setMinimalAmount(Stock stock, StockItem item) {
        try {
            setMinPs.setInt(1, item.getMinimalAmount());
            setMinPs.setInt(2, stock.getOwner().getId().getId());
            setMinPs.setInt(3, item.getProduct().getId().getId());
            if (setMinPs.executeUpdate() == 0) {
                addMinPs.setInt(1, stock.getOwner().getId().getId());
                addMinPs.setInt(2, item.getProduct().getId().getId());
                addMinPs.setInt(3, item.getMinimalAmount());
                addMinPs.executeUpdate();
            }
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public void alterItemsBlocks(Collection<TrackedProduct> items) throws OutOfBoundsException {
        try {
            connection.setAutoCommit(false);
            for (TrackedProduct trackedProduct : items) {
                alterItemBlocks(trackedProduct);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Exceptions.printStackTrace(ex1);
            }
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public void alterItemBlocks(TrackedProduct item) throws OutOfBoundsException {
        try {
            findOnePs.setInt(1, item.getSourceStock().getId().getId());
            findOnePs.setInt(2, item.getProduct().getId().getId());
            ResultSet rs = findOnePs.executeQuery();
            if (rs.next()) {
                int curBlocked = rs.getInt("amount_blocked");
                int setBlocked = curBlocked + item.getAmount();
                int stockAmount = rs.getInt("amount");
                if (setBlocked > stockAmount) {
                    throw new OutOfBoundsException();
                }
                setBlockedPs.setInt(1, setBlocked);
                setBlockedPs.setInt(2, item.getSourceStock().getId().getId());
                setBlockedPs.setInt(3, item.getProduct().getId().getId());
                setBlockedPs.executeUpdate();
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
}
