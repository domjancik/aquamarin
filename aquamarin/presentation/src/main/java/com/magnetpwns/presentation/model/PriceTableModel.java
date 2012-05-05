/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Price;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class PriceTableModel extends GenericTableModel<Price> {

    public PriceTableModel(Collection<Price> data) {
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
                return NbBundle.getMessage(PriceTableModel.class, "TCN_Price");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Price p = items.get(i);
        
        switch (i1) {
            case 0:
                return p.getPrice().toPlainString();
            default:
                return "";
        }
    }
}
