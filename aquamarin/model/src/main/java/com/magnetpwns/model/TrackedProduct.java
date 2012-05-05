/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class TrackedProduct extends CountedProduct {
    Client sourceStock;

    public TrackedProduct(Product product, int count, Client sourceStock) {
        super(product, count);
        this.sourceStock = sourceStock;
    }

    public Client getSourceStock() {
        return sourceStock;
    }
}
