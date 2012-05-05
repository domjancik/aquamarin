/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.City;
import com.magnetpwns.model.CityId;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface CityDAO {
    Collection<City> findAll();
    City find(CityId id);
    City add(City c);
    boolean delete(City c);
    boolean update(City c);
}
