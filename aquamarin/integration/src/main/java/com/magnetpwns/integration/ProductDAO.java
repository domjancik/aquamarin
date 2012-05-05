/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Product;
import com.magnetpwns.model.ProductId;
import com.magnetpwns.model.exception.AquamarinException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public interface ProductDAO {
    Collection<Product> findAll();
    Product find(ProductId id, Date date);
    Product find(ProductId id);
    Product add(Product p);
    boolean delete(Product p);
    boolean update(Product p);
    void addPrice(Product p, BigDecimal price) throws AquamarinException;
    void load(Product p) throws AquamarinException;
}
