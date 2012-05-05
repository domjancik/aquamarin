/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.PriceDAO;
import com.magnetpwns.model.PriceId;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.MissingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 * CityDAO using a derby database with an explicit cache to minimize queries
 * on find.
 * @author MagNet
 */
public class PriceDAODerby implements PriceDAO {

    private static PriceDAODerby instance;
    
    private PreparedStatement findPs;
    
    private Connection connection;
    
    private PriceDAODerby(Connection connection) {
        this.connection = connection;
        try {
            findPs = connection.prepareStatement("SELECT * FROM ALTERNATEPRICE WHERE ID = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static PriceDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new PriceDAODerby(connection);
        }
        return instance;
    }

    @Override
    public BigDecimal find(PriceId id) throws AquamarinException {
        ResultSet rs = null;
        BigDecimal result;
        try {
            findPs.setInt(1, id.getId());
            rs = findPs.executeQuery();
            if (rs.next()) {
                result = rs.getBigDecimal("price");
            } else {
                throw new MissingException();
            }
        } catch (SQLException ex) {
            throw new AquamarinException(ex);
        } finally {
            if (rs != null)
                try {
                rs.close();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
                throw new AquamarinException(ex);
            }
        }
        
        return result;
    }
    

    
}
