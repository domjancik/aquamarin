/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class StockItem extends CountedProduct {
    int minimalAmount;
    int blockedAmount;

    public StockItem(Product product, int count, int minimalAmount, int blockedAmount) {
        super(product, count);
        this.minimalAmount = minimalAmount;
        this.blockedAmount = blockedAmount;
    }

    public int getMinimalAmount() {
        return minimalAmount;
    }

    public void setMinimalAmount(int minimalAmount) {
        this.minimalAmount = minimalAmount;
    }

    public int getBlockedAmount() {
        return blockedAmount;
    }
    
    public int getAvailableAmount() {
        return getAmount() - blockedAmount;
    }
    
    
}
