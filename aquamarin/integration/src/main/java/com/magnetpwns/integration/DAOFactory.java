/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

import org.openide.util.Lookup;

/**
 *
 * @author MagNet
 */
public abstract class DAOFactory implements DAOFactoryInterface {
    private static DAOFactoryInterface instance;
    
    protected DAOFactory() {}
    
    public static DAOFactoryInterface getDefault() {
        if (instance == null) {
            instance = Lookup.getDefault().lookup(DAOFactoryInterface.class);
        }
        return instance;
    }
}
