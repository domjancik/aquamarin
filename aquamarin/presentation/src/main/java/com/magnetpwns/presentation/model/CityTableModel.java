/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.City;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class CityTableModel extends GenericTableModel<City> {

    public CityTableModel(Collection<City> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(CityTableModel.class, "TCN_City");
            case 1:
                return NbBundle.getMessage(CityTableModel.class, "TCN_Postcode");
            case 2:
                return NbBundle.getMessage(CityTableModel.class, "TCN_Country");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        City c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getName();
            case 1:
                return c.getParsedPostcode();
            case 2:
                return c.getCountry();
            default:
                return "";
        }
    }
}
