/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class ProductId extends AbstractId {

    public final static ProductId SPECIFIC = new ProductId(0);
    
    public ProductId(int id) {
        super(id);
    }
    
}
