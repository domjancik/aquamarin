/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.ProformaInvoice;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface ProformaDAO {
    Collection<ProformaInvoice> findAll();
    ProformaInvoice add(ProformaInvoice p);
    ProformaInvoice load(ProformaInvoice p);
}
