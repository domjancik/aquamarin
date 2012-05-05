/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.TaxRate;
import com.magnetpwns.model.TaxRateId;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public interface TaxRateDAO {
    Collection<TaxRate> findAll();
    TaxRate find(TaxRateId id, Date date);
    TaxRate add(TaxRate t);
    boolean delete(TaxRate t);
    boolean update(TaxRate t);
    TaxRate find(TaxRateId id);
}
