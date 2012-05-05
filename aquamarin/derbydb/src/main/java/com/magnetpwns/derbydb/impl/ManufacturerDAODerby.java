/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.ManufacturerDAO;
import com.magnetpwns.model.Manufacturer;
import com.magnetpwns.model.ManufacturerId;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ManufacturerDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class ManufacturerDAODerby implements ManufacturerDAO {

    private static ManufacturerDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    
    private Map<ManufacturerId, Manufacturer> manufacturers;
    private boolean cached;
    
    private ManufacturerDAODerby(Connection connection) {
        manufacturers = new TreeMap<ManufacturerId, Manufacturer>();
        cached = false;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM MANUF");
            findOnePs = connection.prepareStatement("SELECT * FROM MANUF WHERE ID = ?");
            addPs = connection.prepareStatement("INSERT INTO MANUF VALUES (DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM MANUF WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE MANUF SET manuf_name = ? WHERE ID = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ManufacturerDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new ManufacturerDAODerby(connection);
        }
        return instance;
    }
    
    @Override
    public Collection<Manufacturer> findAll() {
        if (!cached) {
            try {
                ResultSet rs = findPs.executeQuery();
                while (rs.next()) {
                    Manufacturer m = new Manufacturer(new ManufacturerId(rs.getInt(1)), rs.getString("manuf_name"));
                    manufacturers.put(m.getId(), m);
                }
                cached = true;
            } catch (SQLException ex) {
                Logger.getLogger(ManufacturerDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<Manufacturer> ManufacturerArray = new ArrayList<Manufacturer>();
        for (Manufacturer Manufacturer : manufacturers.values()) {
            ManufacturerArray.add(Manufacturer);
        }
        
        return ManufacturerArray;
    }

    @Override
    public Manufacturer find(ManufacturerId id) {
        Manufacturer m = manufacturers.get(id);
        if (m == null) {
            try {
                findOnePs.setInt(1, id.getId());
                ResultSet rs = findOnePs.executeQuery();
                if (rs.next()) {
                    m = new Manufacturer(new ManufacturerId(rs.getInt(1)), rs.getString("manuf_name"));
                    manufacturers.put(m.getId(), m);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManufacturerDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return m;
    }

    @Override
    public Manufacturer add(Manufacturer m) {
        try {
            addPs.setString(1, m.getName());
            addPs.executeUpdate();
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                m.setId(new ManufacturerId(rs.getInt(1)));
                manufacturers.put(m.getId(), m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManufacturerDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return m;
    }

    @Override
    public boolean delete(Manufacturer c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
            manufacturers.remove(c.getId());
        } catch (SQLException ex) {
            Logger.getLogger(ManufacturerDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Manufacturer m) {
        try {
            updatePs.setString(1, m.getName());
            updatePs.setInt(2, m.getId().getId());
            updatePs.executeUpdate();
            manufacturers.put(m.getId(), m);
        } catch (SQLException ex) {
            Logger.getLogger(ManufacturerDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
}
