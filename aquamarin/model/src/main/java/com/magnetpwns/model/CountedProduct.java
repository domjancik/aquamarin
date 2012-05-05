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
public class CountedProduct implements Identifiable {
    private Product product;
    private int amount;

    public CountedProduct(Product product, int count) {
        this.product = product;
        this.amount = count;
    }

    public int getAmount() {
        return amount;
    }

    public Product getProduct() {
        return product;
    }
    
    public void alterAmount(int amount) {
        this.amount += amount;
    }
    
    public CountedProduct getInverse() {
        return new CountedProduct(product, -amount);
    }

    @Override
    public AbstractId getId() {
        return product.getId();
    }

    @Override
    public void setId(AbstractId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BigDecimal getTotal() {
        return product.getUntaxedPrice().multiply(new BigDecimal(amount));
    }
    
    public BigDecimal getTaxedTotal() {
        return product.getPrice().multiply(new BigDecimal(amount));
    }
}
