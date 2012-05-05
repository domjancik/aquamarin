/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author MagNet
 */
public class StockTransfer implements Identifiable {
    StockTransferId id;
    Client sourceStock;
    Client destinationStock;
    Date date;
    Collection<CountedProduct> items;
    StockTransferType type;
    String note;

    public StockTransfer(StockTransferId id, Client sourceStock, Client destinationStock, Date date, Collection<CountedProduct> items, StockTransferType type, String note) {
        this.id = id;
        this.sourceStock = sourceStock;
        this.destinationStock = destinationStock;
        this.date = date;
        this.items = items;
        this.type = type;
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public Client getDestinationStock() {
        return destinationStock;
    }

    public Collection<CountedProduct> getItems() {
        return items;
    }

    public Client getSourceStock() {
        return sourceStock;
    }

    @Override
    public StockTransferId getId() {
        return id;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (StockTransferId) id;
    }

    public void setItems(Set<CountedProduct> items) {
        this.items = items;
    }

    public StockTransferType getType() {
        return type;
    }

    public String getNote() {
        return note;
    }
    
    
}
