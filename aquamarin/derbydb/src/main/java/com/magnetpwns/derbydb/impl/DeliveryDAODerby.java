/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.DeliveryDAO;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class DeliveryDAODerby implements DeliveryDAO {

    private static DeliveryDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement addPs;
    private PreparedStatement addItemPs;
    private PreparedStatement loadPs;
    private PreparedStatement findItemPs;
    private PreparedStatement findOneItemPs;
    private PreparedStatement loadOneItemPs;
    private PreparedStatement setItemBlocksPs;
    
    private Map<Integer, Client> cachedClients;
    
    private Connection connection;
    
    // TODO: add transfers from invoiceitem table + returns
    
    private DeliveryDAODerby(Connection connection) {
        this.connection = connection;
        cachedClients = new TreeMap<Integer, Client>();
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM DELIVERY ORDER BY date_issue DESC");
            addPs = connection.prepareStatement("INSERT INTO DELIVERY VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            addItemPs = connection.prepareStatement("INSERT INTO DELIVERYITEM VALUES (?, ?, ?, ?, ?)");
            loadPs = connection.prepareStatement("SELECT * FROM DELIVERY WHERE id = ?");
            findItemPs = connection.prepareStatement("SELECT * FROM DELIVERYITEM WHERE doc_id = ?");
            findOneItemPs = connection.prepareStatement("SELECT * FROM DELIVERYITEM WHERE doc_id = ? AND stock_id = ? AND product_id = ?");
            setItemBlocksPs = connection.prepareStatement("UPDATE DELIVERYITEM SET amount_blocked = ? WHERE doc_id = ? AND stock_id = ? AND product_id = ?");
        } catch (SQLException ex) {
            Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DeliveryDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new DeliveryDAODerby(connection);
        }
        return instance;
    }

    @Override
    public Delivery add(Delivery d) {
        try {
            connection.setAutoCommit(false);
            
            addPs.setInt(1, d.getClient().getId().getId());
            addPs.setDate(2, new java.sql.Date(d.getIssueDate().getTime()));
            addPs.setString(3, d.getNote());
            addPs.setString(4, d.getDeliveryNote());
            addPs.setInt(5, d.checkBlocking());
            addPs.setInt(6, d.getDiscountedTaxedTotalRounded().intValue());
            addPs.executeUpdate();
            
            ResultSet rs = addPs.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            d.setId(new DocumentId(id));
            rs.close();
            for (DeliveryItem di : d.getItems()) {
                addItemPs.setInt(1, d.getId().getId());
                addItemPs.setInt(2, di.getSourceStock().getId().getId());
                addItemPs.setInt(3, di.getProduct().getId().getId());
                addItemPs.setInt(4, di.getAmount());
                addItemPs.setInt(5, di.getBlocked());
                addItemPs.executeUpdate();
                
                StockDAODerby.getInstance(connection).alterItemBlocks(  // TODO: Throw exception
                        new TrackedProduct(di.getProduct(), di.getBlocked(), di.getSourceStock()));
            }
            
            connection.commit();
            
        } catch (OutOfBoundsException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Exceptions.printStackTrace(ex1);
            }
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return d;
    }

    private Client findClient(int id) {
        Client c = cachedClients.get(id);
        if (c == null) {
            c = ClientDAODerby.getInstance(connection).find(new ClientId(id));
            cachedClients.put(id, c);
        }
        return c;
    }
    
    private Delivery getDeliveryFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        Client client = findClient(clientId);
        Date issueDate = new Date(rs.getDate("date_issue").getTime());
        String note = rs.getString("note");
        String deliveryNote = rs.getString("note_delivery");
        int blocking = rs.getInt("blocking");
        int savedTotal = rs.getInt("total");
        return new Delivery(new DocumentId(id), client, issueDate, null, note, deliveryNote, savedTotal, blocking);
    }
    
    @Override
    public Collection<Delivery> findAll() {
        ArrayList<Delivery> items = new ArrayList<Delivery>();
        try {
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                items.add(getDeliveryFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    /**
     * Parse a DeliveryItem from the given result with data valid at the given date
     * @param rs
     * @param date
     * @return
     * @throws SQLException 
     */
    private DeliveryItem getDIFromResult(ResultSet rs, Date date) throws SQLException {
        int productId = rs.getInt("product_id");
        int stockId = rs.getInt("stock_id");
        int amount = rs.getInt("amount");
        int blockedAmount = rs.getInt("amount_blocked");
        
        return new DeliveryItem(
                ProductDAODerby.getInstance(connection).find(new ProductId(productId), date),
                amount,
                ClientDAODerby.getInstance(connection).find(new ClientId(stockId)),
                blockedAmount);
    }
    
    @Override
    public Delivery load(Delivery d) {
        ArrayList<DeliveryItem> items = new ArrayList<DeliveryItem>();
        
        try {    
            findItemPs.setInt(1, d.getId().getId());
            ResultSet rs  = findItemPs.executeQuery();
            while (rs.next()) {
                items.add(getDIFromResult(rs, d.getIssueDate()));
            }
            rs.close();
            d.setItems(items);
        } catch (SQLException ex) {
            Logger.getLogger(DeliveryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return d;
    }

    @Override
    public boolean alterBlocks(Delivery d, Collection<TrackedProduct> items) throws OutOfBoundsException {
        // TODO: no autocommit
        
        try {
            findOneItemPs.setInt(1, d.getId().getId());
            ResultSet rs = null;
            for (TrackedProduct trackedProduct : items) {
                findOneItemPs.setInt(2, trackedProduct.getSourceStock().getId().getId());
                findOneItemPs.setInt(3, trackedProduct.getProduct().getId().getId());
                rs = findOneItemPs.executeQuery();
                if (rs.next()) {
                    int curBlocked = rs.getInt("amount_blocked");
                    int setBlocked = curBlocked + trackedProduct.getAmount();
                    if (setBlocked < 0) {
                        throw new OutOfBoundsException();
                    }
                    StockDAODerby.getInstance(connection).alterItemBlocks(trackedProduct);
                    setItemBlocksPs.setInt(1, setBlocked);
                    setItemBlocksPs.setInt(2, d.getId().getId());
                    setItemBlocksPs.setInt(3, trackedProduct.getSourceStock().getId().getId());
                    setItemBlocksPs.setInt(4, trackedProduct.getProduct().getId().getId());
                    setItemBlocksPs.executeUpdate();
                }
            }
            if (rs != null)
                rs.close();
            return true;
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }
}

