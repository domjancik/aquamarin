/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class Manufacturer implements Identifiable {
    
    public final static Manufacturer DUMMY = new Manufacturer(new ManufacturerId(0), "");
    
    private ManufacturerId id;
    private String name;

    public Manufacturer(ManufacturerId id, String name) {
        this.id = id;
        this.name = name;
    }

    
    @Override
    public ManufacturerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (ManufacturerId) id;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
