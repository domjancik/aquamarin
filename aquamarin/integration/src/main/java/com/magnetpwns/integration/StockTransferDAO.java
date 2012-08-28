/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Product;
import com.magnetpwns.model.StockTransfer;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.NoResultException;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface StockTransferDAO {
    public abstract StockTransfer add(StockTransfer st);
    public abstract Collection<StockTransfer> findAll();
    public abstract Collection<StockTransfer> findByYear(int year);
    public abstract Collection<StockTransfer> findByYearProduct(int year, Product p) throws NoResultException;
    public abstract Collection<Integer> findYears();
    public abstract StockTransfer load(StockTransfer st);
}
