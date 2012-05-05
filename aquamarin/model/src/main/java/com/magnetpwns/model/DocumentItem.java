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
public class DocumentItem extends TrackedProduct {
    private BigDecimal discount;

    public DocumentItem(Product product, int count, BigDecimal discount, Client sourceStock) {
        super(product, count, sourceStock);
        this.discount = discount.setScale(2, RoundingMode.HALF_UP);
        this.sourceStock = sourceStock;
    }
    
    public BigDecimal getDiscountMultiplication() {
        BigDecimal subtrahend = discount.divide(BigDecimalConstant.BD100, 4, RoundingMode.HALF_UP);
        return BigDecimal.ONE.subtract(subtrahend).setScale(4);
    }
    
    public BigDecimal getDiscountedTotal() {
        return getDiscountedProductPrice().multiply(new BigDecimal(getAmount())).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount.setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getDiscountedTaxedTotal() {
        return getTaxedDiscountedProductPrice().multiply(new BigDecimal(getAmount())).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getDiscountedProductPrice() {
        return getDiscountedTaxedProductPrice().divide(getProduct().getTaxRate().getMultiplication(),
                2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getDiscountedTaxedProductPrice() {
        return getProduct().getPrice().multiply(getDiscountMultiplication()).setScale(0, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getTaxedDiscountedProductPrice() {
        return getProduct().getPrice().multiply(getDiscountMultiplication()).setScale(0, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
