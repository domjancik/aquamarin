/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.TaxRateDAO;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.TaxRate;
import com.magnetpwns.model.TaxRateId;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 * TaxRateDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class TaxRateDAODerby implements TaxRateDAO {

    private static TaxRateDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    private PreparedStatement findHisPs;
    private PreparedStatement addHisPs;
    
    private Map<TaxRateId, TaxRate> taxRates;
    private boolean cached;
    
    private TaxRateDAODerby(Connection connection) {
        taxRates = new TreeMap<TaxRateId, TaxRate>();
        cached = false;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM TAXRATE");
            findOnePs = connection.prepareStatement("SELECT * FROM TAXRATE WHERE ID = ?");
            addPs = connection.prepareStatement("INSERT INTO TAXRATE VALUES (DEFAULT, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM TAXRATE WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE TAXRATE SET taxrate_name = ?, taxrate = ? WHERE ID = ?");
            findHisPs = connection.prepareStatement("SELECT taxrate FROM TAXRATEHISTORY WHERE taxrate_id = ? AND valid > ? ORDER BY valid ASC");
            findHisPs.setMaxRows(1);
            addHisPs = connection.prepareStatement("INSERT INTO TAXRATEHISTORY VALUES (?, ?, ?)");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static TaxRateDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new TaxRateDAODerby(connection);
        }
        return instance;
    }
    
    @Override
    public Collection<TaxRate> findAll() {
        if (!cached) {
            try {
                ResultSet rs = findPs.executeQuery();
                while (rs.next()) {
                    TaxRate t = new TaxRate(new TaxRateId(rs.getInt(1)), rs.getString("taxrate_name"), rs.getBigDecimal("taxrate"));
                    taxRates.put(t.getId(), t);
                }
                cached = true;
            } catch (SQLException ex) {
                Logger.getLogger(TaxRateDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<TaxRate> taxRateArray = new ArrayList<TaxRate>();
        for (TaxRate taxRate : taxRates.values()) {
            taxRateArray.add(taxRate);
        }
        
        return taxRateArray;
    }

    @Override
    public TaxRate find(TaxRateId id) {
        TaxRate t = taxRates.get(id);
        if (t == null) {
            try {
                findOnePs.setInt(1, id.getId());
                ResultSet rs = findOnePs.executeQuery();
                if (rs.next()) {
                    t = new TaxRate(new TaxRateId(rs.getInt(1)), rs.getString("taxrate_name"), rs.getBigDecimal("taxrate"));
                    taxRates.put(t.getId(), t);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TaxRateDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return t;
    }

    @Override
    public TaxRate add(TaxRate u) {
        try {
            addPs.setString(1, u.getName());
            addPs.setBigDecimal(2, u.getTaxRate());
            addPs.executeUpdate();
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                u.setId(new TaxRateId(rs.getInt(1)));
                taxRates.put(u.getId(), u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaxRateDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return u;
    }

    @Override
    public boolean delete(TaxRate c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
            taxRates.remove(c.getId());
        } catch (SQLException ex) {
            Logger.getLogger(TaxRateDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    private void addTaxRateHistory(TaxRate t) throws SQLException {
        addHisPs.setInt(1, t.getId().getId());
        addHisPs.setBigDecimal(2, t.getTaxRate());
        addHisPs.setDate(3, new java.sql.Date(new Date().getTime()));
        addHisPs.executeUpdate();
    }
    
    @Override
    public boolean update(TaxRate c) {
        try {
            TaxRate tr = find(c.getId());
            if (tr.getTaxRate() != c.getTaxRate()) {
                addTaxRateHistory(tr);
            }
            
            updatePs.setString(1, c.getName());
            updatePs.setBigDecimal(2, c.getTaxRate());
            updatePs.setInt(3, c.getId().getId());
            updatePs.executeUpdate();
            taxRates.put(c.getId(), c);
        } catch (SQLException ex) {
            Logger.getLogger(TaxRateDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public TaxRate find(TaxRateId id, Date date) {
        TaxRate t = find(id);
        try {
            findHisPs.setInt(1, id.getId());
            findHisPs.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = findHisPs.executeQuery();
            if (rs.next()) {
                t.setTaxRate(rs.getBigDecimal(1));
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        return t;
    }
    
}
