/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.TaxRate;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class TaxRateTableModel extends GenericTableModel<TaxRate> {
    
    public TaxRateTableModel(Collection<TaxRate> data) {
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
                return NbBundle.getMessage(TaxRateTableModel.class, "TCN_TaxRate");
            case 1:
                return NbBundle.getMessage(TaxRateTableModel.class, "TCN_Name");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        TaxRate t = items.get(i);
        
        switch (i1) {
            case 0:
                return t.getTaxRate();
            case 1:
                return t.getName();
            default:
                return "";
        }
    }
}
