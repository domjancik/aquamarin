/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Country;
import com.magnetpwns.model.CountryId;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface CountryDAO {
    Collection<Country> findAll();
    Country find(CountryId id);
    Country add(Country c);
    boolean delete(Country c);
    boolean update(Country c);
}
