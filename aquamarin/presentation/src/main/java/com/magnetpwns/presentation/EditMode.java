/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation;

import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public enum EditMode {
    ADD     (NbBundle.getMessage(AddressBookTopComponent.class, "CTL_FormAdd")),
    EDIT    (NbBundle.getMessage(AddressBookTopComponent.class, "CTL_FormEdit")),
    FILTER  (NbBundle.getMessage(AddressBookTopComponent.class, "CTL_FormFilter")),
    NONE    ("");
    
    private final String label;
    
    EditMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    
}
