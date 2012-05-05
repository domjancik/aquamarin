/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Manufacturer;
import com.magnetpwns.model.ManufacturerId;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface ManufacturerDAO {
    Collection<Manufacturer> findAll();
    Manufacturer add(Manufacturer m);
    boolean delete(Manufacturer m);
    boolean update(Manufacturer m);
    Manufacturer find(ManufacturerId id);
}
