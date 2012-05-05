/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.ClientDAO;
import com.magnetpwns.model.CityId;
import com.magnetpwns.model.Client;
import com.magnetpwns.model.ClientId;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.MissingException;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class ClientDAODerby implements ClientDAO {

    private static ClientDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement addPs;
    private PreparedStatement addManualIdPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    private PreparedStatement updatePaidPs;
    private PreparedStatement findSOPs;
    private PreparedStatement filterPs;
    
    private Connection connection;
    
    private ClientDAODerby(Connection connection) {
        this.connection = connection;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM CLIENT ORDER BY id ASC");
            findOnePs = connection.prepareStatement("SELECT * FROM CLIENT WHERE id = ?");
            findSOPs = connection.prepareStatement("SELECT * FROM CLIENT WHERE stock_enabled = 1");
            addPs = connection.prepareStatement("INSERT INTO CLIENT VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            addManualIdPs = connection.prepareStatement("INSERT INTO CLIENT VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            deletePs = connection.prepareStatement("DELETE FROM CLIENT WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE CLIENT SET client_title = ?, client_title_short = ?, client_name = ?, vip = ?, street = ?, ico = ?, dic = ?, telephone_work = ?, telephone_home = ?, telephone_cell = ?, email = ?, note = ?, tempsum = ?, city_id = ?, stock_enabled = ? WHERE ID = ?");
            updatePaidPs = connection.prepareStatement("UPDATE CLIENT SET tempsum = ? WHERE ID = ?");
            filterPs = connection.prepareStatement("SELECT * FROM CLIENT WHERE client_title LIKE ?");
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ClientDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new ClientDAODerby(connection);
        }
        return instance;
    }
    
    private Client getClientFromResult(ResultSet rs) throws SQLException {
        return new Client(new ClientId(rs.getInt(1)),
                rs.getString("client_title"),
                rs.getString("client_title_short"),
                rs.getString("client_name"),
                rs.getBoolean("vip"),
                rs.getString("street"),
                rs.getInt("ico"),
                rs.getString("dic"),
                rs.getString("telephone_work"),
                rs.getString("telephone_home"),
                rs.getString("telephone_cell"),
                rs.getString("email"),
                rs.getString("note"),
                CityDAODerby.getInstance(connection).find(
                    new CityId(rs.getInt("city_id"))),
                rs.getBigDecimal("tempsum"),
                rs.getBoolean("stock_enabled"));
    }

    @Override
    public Client find(ClientId id) {
        try {
            findOnePs.setInt(1, id.getId());
            ResultSet rs = findOnePs.executeQuery();
            rs.next();
            Client c = getClientFromResult(rs);
            rs.close();
            return c;
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private Collection<Client> baseFind(PreparedStatement ps) {
        Collection<Client> clients = new ArrayList<Client>();
        try {    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clients.add(getClientFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }
    
    @Override
    public Collection<Client> findAll() {
        return baseFind(findPs);
    }

    @Override
    public Collection<Client> findFiltered(Client c) {
        try {
            filterPs.setString(1, c.getTitle() + "%");
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        return baseFind(filterPs);
    }

    @Override
    public Collection<Client> findAllStockOwners() {
        Collection<Client> clients = new ArrayList<Client>();
        try {    
            ResultSet rs = findSOPs.executeQuery();
            while (rs.next()) {
                clients.add(getClientFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }

    @Override
    public Client add(Client c) {
        try {
            int i = 1;
            PreparedStatement aPs;
            if (c.getId().getId() == 0) {
                aPs = addPs;
            } else {
                aPs = addManualIdPs;
                aPs.setInt(i++, c.getId().getId());
            }
            aPs.setString(i++, c.getTitle());
            aPs.setString(i++, c.getTitleShort());
            aPs.setString(i++, c.getName());            
            aPs.setBoolean(i++, c.isVip());
            aPs.setString(i++, c.getStreet());
            aPs.setInt(i++, c.getIco());
            aPs.setString(i++, c.getDic());
            aPs.setString(i++, c.getTelephoneWork());
            aPs.setString(i++, c.getTelephoneHome());
            aPs.setString(i++, c.getTelephoneCell());
            aPs.setString(i++, c.getEmail());
            aPs.setString(i++, c.getNote());
            aPs.setBigDecimal(i++, c.getTempSum());
            aPs.setInt(i++, c.getCity().getId().getId());
            aPs.setBoolean(i++, c.isStockEnabled());
            aPs.executeUpdate();
            
            if (c.getId().getId() != 0) {
                Statement s = connection.createStatement();
                s.executeUpdate("ALTER TABLE CLIENT ALTER COLUMN ID RESTART WITH "
                        + Integer.toString(c.getId().getId() + 1));
            } else {
                ResultSet rs = aPs.getGeneratedKeys();
                if (rs.next()) {
                    c.setId(new ClientId(rs.getInt(1)));
                }
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return c;
    }

    @Override
    public boolean delete(Client c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Client c) {
        try {
            updatePs.setString(1, c.getTitle());
            updatePs.setString(2, c.getTitleShort());
            updatePs.setString(3, c.getName());
            updatePs.setBoolean(4, c.isVip());
            updatePs.setString(5, c.getStreet());
            updatePs.setInt(6, c.getIco());
            updatePs.setString(7, c.getDic());
            updatePs.setString(8, c.getTelephoneWork());
            updatePs.setString(9, c.getTelephoneHome());
            updatePs.setString(10, c.getTelephoneCell());
            updatePs.setString(11, c.getEmail());
            updatePs.setString(12, c.getNote());
            updatePs.setBigDecimal(13, c.getTempSum());
            updatePs.setInt(14, c.getCity().getId().getId());
            updatePs.setBoolean(15, c.isStockEnabled());
            updatePs.setInt(16, c.getId().getId());
            updatePs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public void alterPaid(Client c, BigDecimal amount) throws AquamarinException {
        try {
            findOnePs.setInt(1, c.getId().getId());
            ResultSet rs = findOnePs.executeQuery();
            BigDecimal paid;
            if (rs.next()) {
                paid = rs.getBigDecimal("tempsum");
            } else {
                throw new MissingException();
            }
            
            paid = paid.add(amount);
            
            if (paid.compareTo(BigDecimal.ZERO) < 0)
                throw new OutOfBoundsException();
            
            updatePaidPs.setBigDecimal(1, paid);
            updatePaidPs.setInt(2, c.getId().getId());
            updatePaidPs.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        }
    }
    
    public void updateTotals() {
        try {
            ResultSet rs = findPs.executeQuery();
            PreparedStatement updateTotalPs = connection.prepareStatement(
                    "UPDATE CLIENT SET tempsum = (SELECT COALESCE(SUM(amount),0) FROM PAYMENT p JOIN INVOICE i ON p.doc_id = i.id WHERE client_id = ?) WHERE id = ?");
            while (rs.next()) {
                int clientId = rs.getInt("ID");
                updateTotalPs.setInt(1, clientId);
                updateTotalPs.setInt(2, clientId);
                updateTotalPs.executeUpdate();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
