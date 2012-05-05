/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class Unit implements Identifiable {
    private UnitId id;
    private String name;

    public Unit(UnitId id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public UnitId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (UnitId) id;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
