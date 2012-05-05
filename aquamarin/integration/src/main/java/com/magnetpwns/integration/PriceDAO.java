/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.PriceId;
import com.magnetpwns.model.exception.AquamarinException;
import java.math.BigDecimal;

/**
 *
 * @author MagNet
 */
public interface PriceDAO {
    public BigDecimal find(PriceId id) throws AquamarinException;
}
