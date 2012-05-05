/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author MagNet
 */
public class Price implements Identifiable {
    private PriceId id;
    private BigDecimal price;
    
    public final static PriceId DEFAULT = new PriceId(0);

    public Price(PriceId id, BigDecimal price) {
        this.id = id;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    @Override
    public PriceId getId() {
        return id;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (PriceId) id;
    }
}
