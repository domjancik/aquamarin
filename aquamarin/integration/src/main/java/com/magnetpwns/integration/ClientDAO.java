/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;


import com.magnetpwns.model.Client;
import com.magnetpwns.model.ClientId;
import com.magnetpwns.model.exception.AquamarinException;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface ClientDAO {
    Collection<Client> findAll();
    Client find(ClientId id);
    Collection<Client> findFiltered(Client c);
    Client add(Client c);
    boolean delete(Client c);
    boolean update(Client c);
    void alterPaid(Client c, BigDecimal amount) throws AquamarinException;
    
    Collection<Client> findAllStockOwners();
}
