/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.util.Set;

/**
 *
 * @author MagNet
 */
public class Stock implements Identifiable {
    
    private Client owner;
    private Set<StockItem> items;

    public Stock(Client owner, Set<StockItem> items) {
        this.owner = owner;
        this.items = items;
    }

    @Override
    public ClientId getId() {
        return owner.getId();
    }

    public Set<StockItem> getItems() {
        return items;
    }

    public Client getOwner() {
        return owner;
    }

    @Override
    public void setId(AbstractId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
