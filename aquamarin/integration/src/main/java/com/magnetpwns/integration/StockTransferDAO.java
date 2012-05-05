/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.StockTransfer;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface StockTransferDAO {
    public abstract StockTransfer add(StockTransfer st);
    public abstract Collection<StockTransfer> findAll();
    public abstract Collection<StockTransfer> findByYear(int year);
    public abstract Collection<Integer> findYears();
    public abstract StockTransfer load(StockTransfer st);
}
