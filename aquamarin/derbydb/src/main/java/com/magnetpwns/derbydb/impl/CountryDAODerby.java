/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.CountryDAO;
import com.magnetpwns.model.Country;
import com.magnetpwns.model.CountryId;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CountryDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class CountryDAODerby implements CountryDAO {

    private static CountryDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    
    private Map<CountryId, Country> countries;
    private boolean cached;
    
    private CountryDAODerby(Connection connection) {
        countries = new TreeMap<CountryId, Country>();
        cached = false;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM COUNTRY");
            findOnePs = connection.prepareStatement("SELECT * FROM COUNTRY WHERE ID = ?");
            addPs = connection.prepareStatement("INSERT INTO COUNTRY VALUES (DEFAULT, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM COUNTRY WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE COUNTRY SET country_name = ? WHERE ID = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CountryDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new CountryDAODerby(connection);
        }
        return instance;
    }
    
    @Override
    public Collection<Country> findAll() {
        if (!cached) {
            try {
                ResultSet rs = findPs.executeQuery();
                while (rs.next()) {
                    Country c = new Country(new CountryId(rs.getInt(1)), rs.getString("country_name"));
                    countries.put(c.getId(), c);
                }
                cached = true;
            } catch (SQLException ex) {
                Logger.getLogger(CountryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<Country> countryArray = new ArrayList<Country>();
        for (Country country : countries.values()) {
            countryArray.add(country);
        }
        
        return countryArray;
    }

    @Override
    public Country find(CountryId id) {
        Country u = countries.get(id);
        if (u == null) {
            try {
                findOnePs.setInt(1, id.getId());
                ResultSet rs = findOnePs.executeQuery();
                if (rs.next()) {
                    u = new Country(new CountryId(rs.getInt(1)), rs.getString("country_name"));
                    countries.put(u.getId(), u);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CountryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return u;
    }

    @Override
    public Country add(Country u) {
        try {
            addPs.setString(1, u.getName());
            addPs.executeUpdate();
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                u.setId(new CountryId(rs.getInt(1)));
                countries.put(u.getId(), u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return u;
    }

    @Override
    public boolean delete(Country c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
            countries.remove(c.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Country c) {
        try {
            updatePs.setString(1, c.getName());
            updatePs.setInt(2, c.getId().getId());
            updatePs.executeUpdate();
            countries.put(c.getId(), c);
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
}
