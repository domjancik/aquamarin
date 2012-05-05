/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import java.util.Collection;

/**
 *
 * @author MagNet
 */
public interface FacadeMap<T> {
    Collection<T> findAll();
    T add(T o);
    boolean delete(T o);
    boolean update(T o);
}
