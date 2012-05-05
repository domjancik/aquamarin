/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface StockDAO {
    public Stock find(Client id);
    /**
     * Atomic operation to put or remove items in a single stock.
     * @param target the stock
     * @param items the items to be altered
     * @return true if successful
     */
    public abstract boolean alterItems(Stock target, Collection<CountedProduct> items) throws OutOfBoundsException;
    public abstract void alterItem(TrackedProduct item) throws AquamarinException;
    /**
     * Atomic operation for transferring items between two stocks.
     * @param source the source stock
     * @param target the target stock
     * @param items the items to be transferred
     * @return true if successful
     */
    public abstract boolean transferItems(Stock source, Stock target, Collection<CountedProduct> items);
    public abstract boolean setMinimalAmount(Stock stock, StockItem item);
    public abstract void alterItemsBlocks(Collection<TrackedProduct> items) throws OutOfBoundsException;
    public abstract void alterItemBlocks(TrackedProduct item) throws OutOfBoundsException;
}
