/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;

/**
 *
 * @author MagNet
 */
public class DeliveryItem extends DocumentItem {
    private int blocked;

    public DeliveryItem(Product product, int count, Client sourceStock, int blocked) {
        super(product, count, BigDecimal.ZERO, sourceStock);
        this.blocked = blocked;
    }

    public int getBlocked() {
        return blocked;
    }
    
    public void alterBlocks(int amount) {
        blocked += amount;
    }
}
