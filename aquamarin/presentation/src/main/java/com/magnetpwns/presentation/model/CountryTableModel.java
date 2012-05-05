/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Country;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class CountryTableModel extends GenericTableModel<Country> {

    public CountryTableModel(Collection<Country> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(CountryTableModel.class, "TCN_Id");
            case 1:
                return NbBundle.getMessage(CountryTableModel.class, "TCN_Name");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Country c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getId().getId();
            case 1:
                return c.getName();
            default:
                return "";
        }
    }
}
