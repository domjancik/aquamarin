/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.UnitDAO;
import com.magnetpwns.model.Unit;
import com.magnetpwns.model.UnitId;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UnitDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class UnitDAODerby implements UnitDAO {

    private static UnitDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    
    private Map<UnitId, Unit> units;
    private boolean cached;
    
    private UnitDAODerby(Connection connection) {
        units = new TreeMap<UnitId, Unit>();
        cached = false;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM UNIT");
            findOnePs = connection.prepareStatement("SELECT * FROM UNIT WHERE ID = ?");
            addPs = connection.prepareStatement("INSERT INTO UNIT VALUES (DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM UNIT WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE UNIT SET unit_name = ? WHERE ID = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static UnitDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new UnitDAODerby(connection);
        }
        return instance;
    }
    
    @Override
    public Collection<Unit> findAll() {
        if (!cached) {
            try {
                ResultSet rs = findPs.executeQuery();
                while (rs.next()) {
                    Unit c = new Unit(new UnitId(rs.getInt(1)), rs.getString("unit_name"));
                    units.put(c.getId(), c);
                }
                cached = true;
            } catch (SQLException ex) {
                Logger.getLogger(UnitDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<Unit> unitArray = new ArrayList<Unit>();
        for (Unit unit : units.values()) {
            unitArray.add(unit);
        }
        
        return unitArray;
    }

    @Override
    public Unit find(UnitId id) {
        Unit u = units.get(id);
        if (u == null) {
            try {
                findOnePs.setInt(1, id.getId());
                ResultSet rs = findOnePs.executeQuery();
                if (rs.next()) {
                    u = new Unit(new UnitId(rs.getInt(1)), rs.getString("unit_name"));
                    units.put(u.getId(), u);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UnitDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return u;
    }

    @Override
    public Unit add(Unit u) {
        try {
            addPs.setString(1, u.getName());
            addPs.executeUpdate();
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                u.setId(new UnitId(rs.getInt(1)));
                units.put(u.getId(), u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return u;
    }

    @Override
    public boolean delete(Unit c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
            units.remove(c.getId());
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Unit c) {
        try {
            updatePs.setString(1, c.getName());
            updatePs.setInt(2, c.getId().getId());
            updatePs.executeUpdate();
            units.put(c.getId(), c);
        } catch (SQLException ex) {
            Logger.getLogger(UnitDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
}
