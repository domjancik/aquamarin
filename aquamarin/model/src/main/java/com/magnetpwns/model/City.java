/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class City implements Identifiable {
    
    public static final City DUMMY = new City(new CityId(0), 0, "", Country.DUMMY);
    
    private CityId id;
    private int postcode;
    private String name;
    private Country country;

    public City(CityId id, int postcode, String name, Country country) {
        this.id = id;
        this.postcode = postcode;
        this.name = name;
        this.country = country;
    }

    @Override
    public CityId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPostcode() {
        return postcode;
    }
    
    public String getParsedPostcode() {
        String p = Integer.toString(postcode);
        if (p.length() == 5) {
            return p.substring(0, 3) + " " + p.substring(3);
        } else {
            return p;
        }
    }

    public Country getCountry() {
        return country;
    }
    
    @Override
    public String toString() {
        return name + " (" + getParsedPostcode() + ")";
    }
    
    public String toPostString() {
        return getParsedPostcode() + " " + name;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (CityId) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final City other = (City) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}
