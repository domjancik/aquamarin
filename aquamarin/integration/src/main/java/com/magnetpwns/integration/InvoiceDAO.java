/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.Payment;
import com.magnetpwns.model.exception.AquamarinException;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public interface InvoiceDAO {
    Collection<Invoice> findAll();
    Invoice add(Invoice i);
    Invoice load(Invoice i);
    void cancel(Invoice i, Date date) throws AquamarinException;
    void pay(Invoice i, Payment p) throws AquamarinException;
}
