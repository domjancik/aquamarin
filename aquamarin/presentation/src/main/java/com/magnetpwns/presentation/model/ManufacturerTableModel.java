/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Manufacturer;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ManufacturerTableModel extends GenericTableModel<Manufacturer> {
    
    public ManufacturerTableModel(Collection<Manufacturer> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(ManufacturerTableModel.class, "TCN_Name");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Manufacturer m = items.get(i);
        
        switch (i1) {
            case 0:
                return m.getName();
            default:
                return "";
        }
    }
}
