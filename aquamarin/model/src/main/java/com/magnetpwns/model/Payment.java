/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public class Payment implements Identifiable {
    private Date date;
    private String log;
    private BigDecimal amount;

    public Payment(Date date, String log, BigDecimal amount) {
        this.date = date;
        this.log = log;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getLog() {
        return log;
    }

    @Override
    public AbstractId getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setId(AbstractId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
