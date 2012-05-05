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
public class StockTableModel extends GenericTableModel<Client> {

    public StockTableModel(Collection<Client> data) {
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
                return NbBundle.getMessage(StockTableModel.class, "TCN_Id");
            case 1:
                return NbBundle.getMessage(AddressTableModel.class, "TCN_Name");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Client p = items.get(i);
        
        switch (i1) {
            case 0:
                return p.getId().getId();
            case 1:
                return p.toString();
            default:
                return "";
        }
    }
}
