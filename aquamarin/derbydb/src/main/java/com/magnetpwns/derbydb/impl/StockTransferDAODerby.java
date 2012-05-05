/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.StockTransferDAO;
import com.magnetpwns.model.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MagNet
 */
public class StockTransferDAODerby implements StockTransferDAO {

    private static StockTransferDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findByYearPs;
    private PreparedStatement findYearsPs;
    private PreparedStatement addPs;
    private PreparedStatement addItemPs;
    private PreparedStatement loadPs;
    
    private Map<Integer, Client> cachedClients;
    //private Client defaultClient = new Client(new ClientId(0),
    //        "- přídáno -", "", "", false, null, 0, null, null, null, null, null, null, null, 0, false);
    
    private Connection connection;
    
    // TODO: add transfers from invoiceitem table + returns
    
    private StockTransferDAODerby(Connection connection) {
        this.connection = connection;
        cachedClients = new TreeMap<Integer, Client>();
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM STOCKTRANSFER ORDER BY transfer_date DESC");
            findByYearPs = connection.prepareStatement("SELECT * FROM STOCKTRANSFER WHERE YEAR(transfer_date) = ? ORDER BY transfer_date DESC");
            findYearsPs = connection.prepareStatement("SELECT DISTINCT YEAR(transfer_date) FROM STOCKTRANSFER");
            addPs = connection.prepareStatement("INSERT INTO STOCKTRANSFER VALUES (DEFAULT, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            addItemPs = connection.prepareStatement("INSERT INTO STOCKTRANSFERITEM VALUES (?, ?, ?)");
            loadPs = connection.prepareStatement("SELECT * FROM STOCKTRANSFERITEM WHERE transfer_id = ?");
        } catch (SQLException ex) {
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static StockTransferDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new StockTransferDAODerby(connection);
        }
        return instance;
    }

    @Override
    public StockTransfer add(StockTransfer st) {
        try {
            connection.setAutoCommit(false);
            if (st.getSourceStock() == null) {
                addPs.setNull(1, Types.INTEGER);
            } else {
                addPs.setInt(1, st.getSourceStock().getId().getId());
            }
            addPs.setInt(2, st.getDestinationStock().getId().getId());
            addPs.setDate(3, new java.sql.Date(st.getDate().getTime()));
            addPs.setString(4, st.getType().name());
            addPs.setString(5, st.getNote());
            addPs.executeUpdate();
            
            ResultSet rs = addPs.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            st.setId(new StockTransferId(id));
            
            for (CountedProduct cp : st.getItems()) {
                addItemPs.setInt(1, st.getId().getId());
                addItemPs.setInt(2, cp.getProduct().getId().getId());
                addItemPs.setInt(3, cp.getAmount());
                addItemPs.executeUpdate();
            }
            
            connection.commit();
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return st;
    }

    private Client findClient(int id) {
        Client c = cachedClients.get(id);
        if (c == null) {
            c = ClientDAODerby.getInstance(connection).find(new ClientId(id));
            cachedClients.put(id, c);
        }
        return c;
    }
    
    private StockTransfer getStockTransferFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int sourceId = rs.getInt("src_id");
        int destId = rs.getInt("dst_id");
        String typeName = rs.getString("transfer_type");
        String note = rs.getString("note");
        StockTransferType type = StockTransferType.valueOf(typeName);
        Client sourceClient;
        
        if (sourceId == 0) {
            sourceClient = null;
        } else {
            sourceClient = findClient(sourceId);
        }
        
        Client destClient = findClient(destId);
        
        Date date = new Date(rs.getDate("transfer_date").getTime());
        return new StockTransfer(new StockTransferId(id), sourceClient, destClient, date, null, type, note);
    }
    
    @Override
    public Collection<StockTransfer> findAll() {
        ArrayList<StockTransfer> items = new ArrayList<StockTransfer>();
        try {
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                items.add(getStockTransferFromResult(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    @Override
    public Collection<StockTransfer> findByYear(int year) {
        ArrayList<StockTransfer> items = new ArrayList<StockTransfer>();
        try {
            findByYearPs.setInt(1, year);
            ResultSet rs = findByYearPs.executeQuery();
            while (rs.next()) {
                items.add(getStockTransferFromResult(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    private CountedProduct getCPFromResult(ResultSet rs) throws SQLException {
        int productId = rs.getInt("product_id");
        int amount = rs.getInt("amount");
        
        return new CountedProduct(
                ProductDAODerby.getInstance(connection).find(new ProductId(productId)),
                amount);
    }
    
    @Override
    public StockTransfer load(StockTransfer st) {
        Set<CountedProduct> items = new HashSet<CountedProduct>();
        
        try {    
            loadPs.setInt(1, st.getId().getId());
            ResultSet rs  = loadPs.executeQuery();
            while (rs.next()) {
                items.add(getCPFromResult(rs));
            }
            st.setItems(items);
        } catch (SQLException ex) {
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return st;
    }

    @Override
    public Collection<Integer> findYears() {
        ArrayList<Integer> years = new ArrayList<Integer>();
        try {
            ResultSet rs = findYearsPs.executeQuery();
            while (rs.next()) {
                years.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockTransferDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return years;
    }

    
    
}

