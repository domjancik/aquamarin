/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import com.magnetpwns.model.Unit;
import com.magnetpwns.model.UnitId;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface UnitDAO {
    Collection<Unit> findAll();
    Unit add(Unit u);
    boolean delete(Unit u);
    boolean update(Unit u);
    Unit find(UnitId id);
}
