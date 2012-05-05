/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Tax rate category
 * @author MagNet
 */
public class TaxRate implements Identifiable {
    
    private TaxRateId id;
    private String name;
    private BigDecimal taxRate;

    public TaxRate(TaxRateId id, String name, BigDecimal taxRate) {
        this.id = id;
        this.name = name;
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public TaxRateId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (TaxRateId) id;
    }

    @Override
    public String toString() {
        return toPercentString() + " - " + name;
    }
    
    public String toPercentString() {
        return taxRate.setScale(0, RoundingMode.HALF_UP).toPlainString() + "%";
    }
    
    public BigDecimal getMultiplication() {
        return taxRate.divide(BigDecimalConstant.BD100, 4, RoundingMode.HALF_UP).add(BigDecimal.ONE).setScale(4, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getTaxOnlyMultiplication() {
        return taxRate.divide(BigDecimalConstant.BD100, 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP);
    }
    
    
}
