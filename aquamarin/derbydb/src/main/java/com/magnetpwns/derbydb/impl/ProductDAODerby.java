/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.ProductDAO;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class ProductDAODerby implements ProductDAO {

    private static ProductDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement findOnePs;
    private PreparedStatement findAPPs;
    private PreparedStatement addAPPs;
    private PreparedStatement addPs;
    private PreparedStatement deletePs;
    private PreparedStatement updatePs;
    private PreparedStatement findHisPs;
    private PreparedStatement addHisPs;
    
    private Connection connection;
    
    private ProductDAODerby(Connection connection) {
        this.connection = connection;
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM PRODUCT ORDER BY product_code ASC");
            findOnePs = connection.prepareStatement("SELECT * FROM PRODUCT WHERE id = ?");
            findAPPs = connection.prepareStatement("SELECT * FROM ALTERNATEPRICE WHERE product_id = ?");
            addAPPs = connection.prepareStatement("INSERT INTO ALTERNATEPRICE VALUES (DEFAULT, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            addPs = connection.prepareStatement("INSERT INTO PRODUCT VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            deletePs = connection.prepareStatement("DELETE FROM PRODUCT WHERE ID = ?");
            updatePs = connection.prepareStatement("UPDATE PRODUCT SET product_code = ?, product_code_manuf = ?, product_name = ?, description = ?, price = ?, price_bought = ?, manuf_id = ?, tax_id = ?, unit_id = ? WHERE ID = ?");
            findHisPs = connection.prepareStatement("SELECT price FROM PRICEHISTORY WHERE product_id = ? AND valid > ? ORDER BY valid ASC");
            findHisPs.setMaxRows(1);
            addHisPs = connection.prepareStatement("INSERT INTO PRICEHISTORY VALUES (?, ?, ?)");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ProductDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new ProductDAODerby(connection);
        }
        return instance;
    }
    
    private Product getProductFromResult(ResultSet rs) throws SQLException {
        return new Product(new ProductId(rs.getInt(1)),
                        rs.getString("product_code"),
                        rs.getString("product_code_manuf"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getBigDecimal("price_bought"),
                        ManufacturerDAODerby.getInstance(connection).find(
                            new ManufacturerId(rs.getInt("manuf_id"))),
                        TaxRateDAODerby.getInstance(connection).find(
                            new TaxRateId(rs.getInt("tax_id"))),
                        UnitDAODerby.getInstance(connection).find(
                            new UnitId(rs.getInt("unit_id"))));
    }
    
    @Override
    public Collection<Product> findAll() {
        Collection<Product> products = new ArrayList<Product>();
        try {    
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                products.add(getProductFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    @Override
    public Product find(ProductId id) {
        try {
            findOnePs.setInt(1, id.getId());
            ResultSet rs = findOnePs.executeQuery();
            Product p = null;
            if (rs.next()) {
                p = getProductFromResult(rs);
            }
            return p;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    

    @Override
    public Product add(Product p) {
        try {
            int i = 1;
            addPs.setString(i++, p.getCode());
            addPs.setString(i++, p.getCodeManuf());
            addPs.setString(i++, p.getName());            
            addPs.setString(i++, p.getDescription());
            addPs.setBigDecimal(i++, p.getPrice());
            addPs.setBigDecimal(i++, p.getPriceBought());
            addPs.setInt(i++, p.getManufacturer().getId().getId());
            addPs.setInt(i++, p.getTaxRate().getId().getId());
            addPs.setInt(i++, p.getUnit().getId().getId());
            addPs.executeUpdate();
            
            ResultSet rs = addPs.getGeneratedKeys();
            if (rs.next()) {
                p.setId(new ProductId(rs.getInt(1)));
            }
            rs.close();            
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return p;
    }

    @Override
    public boolean delete(Product c) {
        try {
            deletePs.setInt(1, c.getId().getId());
            deletePs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private void addPriceHistory(Product p) throws SQLException {
        addHisPs.setInt(1, p.getId().getId());
        addHisPs.setBigDecimal(2, p.getPrice());
        addHisPs.setDate(3, new java.sql.Date(new Date().getTime()));
    }
    
    @Override
    public boolean update(Product p) {
        try {
            Product oldP = find(p.getId());
            
            if (p.getPrice() != oldP.getPrice()) {
                addPriceHistory(oldP);
            }
            
            int i = 1;
            updatePs.setString(i++, p.getCode());
            updatePs.setString(i++, p.getCodeManuf());
            updatePs.setString(i++, p.getName());            
            updatePs.setString(i++, p.getDescription());
            updatePs.setBigDecimal(i++, p.getPrice());
            updatePs.setBigDecimal(i++, p.getPriceBought());
            updatePs.setInt(i++, p.getManufacturer().getId().getId());
            updatePs.setInt(i++, p.getTaxRate().getId().getId());
            updatePs.setInt(i++, p.getUnit().getId().getId());
            updatePs.setInt(i++, p.getId().getId());
            updatePs.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public Product find(ProductId id, Date date) {
        Product p = find(id);
        try {
            findHisPs.setInt(1, id.getId());
            findHisPs.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = findHisPs.executeQuery();
            if (rs.next()) {
                p.setPrice(rs.getBigDecimal(1));
            }
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
        p.setTaxRate(TaxRateDAODerby.getInstance(connection).find(p.getTaxRate().getId(), date));
        return p;
    }

    @Override
    public void addPrice(Product p, BigDecimal price) throws AquamarinException {
        try {
            addAPPs.setInt(1, p.getId().getId());
            addAPPs.setBigDecimal(2, price);
            addAPPs.executeUpdate();
            ResultSet rs = addAPPs.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            rs.close();
            // TODO ADD PRICE
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        }
    }
    
    @Override
    public void load(Product p) throws AquamarinException {
        try {
            Map<PriceId, BigDecimal> prices = new HashMap<PriceId, BigDecimal>();
            findAPPs.setInt(1, p.getId().getId());
            ResultSet rs = findAPPs.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                BigDecimal price = rs.getBigDecimal("price");
                prices.put(new PriceId(id), price);
            }
            
            p.setAltPrices(prices);
            rs.close();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        }
    }
    
}
