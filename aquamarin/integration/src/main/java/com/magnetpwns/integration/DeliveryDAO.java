/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Delivery;
import com.magnetpwns.model.TrackedProduct;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface DeliveryDAO {
    Collection<Delivery> findAll();
    Delivery add(Delivery d);
    Delivery load(Delivery d);
    boolean alterBlocks(Delivery d, Collection<TrackedProduct> items) throws OutOfBoundsException;
}
