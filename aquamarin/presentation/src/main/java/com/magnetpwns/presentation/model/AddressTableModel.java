/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Client;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class AddressTableModel extends GenericTableModel<Client> {
    
    public AddressTableModel(Collection<Client> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Id");
            case 1:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Title");
            case 2:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Name");
            case 3:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Street");
            case 4:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_City");
            case 5:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Sum");
            case 6:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_VIP");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Client c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getId().getId();
            case 1:
                return c.getTitle();
            case 2:
                return c.getName();
            case 3:
                return c.getStreet();
            case 4:
                return c.getCity().toString();
            case 5:
                return c.getTempSum();
            case 6:
                return c.isVip();
            default:
                return "";
        }
    }
}
