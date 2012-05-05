/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.CityDAO;
import com.magnetpwns.model.City;
import com.magnetpwns.model.CityId;
import com.magnetpwns.model.CountryId;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CityDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class CityDAODerby implements CityDAO {

    private static CityDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    
    private Map<CityId, City> cities;
    private boolean cached;
    
    private Connection connection;
    
    private CityDAODerby(Connection connection) {
        cities = new TreeMap<CityId, City>();
        cached = false;
        this.connection = connection;
        try {
            findPs = connection.prepareStatement("SELECT * FROM CITY ORDER BY cityname ASC");
            findOnePs = connection.prepareStatement("SELECT * FROM CITY WHERE ID = ?");
            addPs = connection.prepareStatement("INSERT INTO CITY VALUES (DEFAULT, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM CITY WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE CITY SET cityname = ?, postcode = ?, country_id = ? WHERE ID = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CityDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new CityDAODerby(connection);
        }
        return instance;
    }
    
    private City getCityFromResult(ResultSet rs) throws SQLException {
        CountryId countryId = new CountryId(rs.getInt("country_id"));
        City c = new City(new CityId(rs.getInt(1)), rs.getInt("postcode"), rs.getString("cityname"), CountryDAODerby.getInstance(connection).find(countryId));
        return c;
    }
    
    @Override
    public Collection<City> findAll() {
        if (!cached) {
            try {
                ResultSet rs = findPs.executeQuery();
                while (rs.next()) {
                    City c = getCityFromResult(rs);
                    cities.put(c.getId(), c);
                }
                cached = true;
            } catch (SQLException ex) {
                Logger.getLogger(CityDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<City> cityArray = new ArrayList<City>();
        for (City city : cities.values()) {
            cityArray.add(city);
        }
        
        return cityArray;
    }

    @Override
    public City find(CityId id) {
        City c = cities.get(id);
        if (c == null) {
            try {
                findOnePs.setInt(1, id.getId());
                ResultSet rs = findOnePs.executeQuery();
                if (rs.next()) {
                    c = getCityFromResult(rs);
                    cities.put(c.getId(), c);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CityDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return c;
    }

    @Override
    public City add(City c) {
        try {
            addPs.setString(1, c.getName());
            addPs.setInt(2, c.getPostcode());
            addPs.setInt(3, c.getCountry().getId().getId());
            addPs.executeUpdate();
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                c.setId(new CityId(rs.getInt(1)));
                cities.put(c.getId(), c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return c;
    }

    @Override
    public boolean delete(City c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
            cities.remove(c.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CityDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(City c) {
        try {
            updatePs.setString(1, c.getName());
            updatePs.setInt(2, c.getPostcode());
            updatePs.setInt(3, c.getId().getId());
            updatePs.setInt(4, c.getCountry().getId().getId());
            updatePs.executeUpdate();
            cities.put(c.getId(), c);
        } catch (SQLException ex) {
            Logger.getLogger(CityDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
}
