/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class Country implements Identifiable {
    
    public static final Country DUMMY = new Country(new CountryId(0), "");
    
    CountryId id;
    String name;

    public Country(CountryId id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public CountryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (CountryId) id;
    }

    @Override
    public String toString() {
        return name;
    }
}
