/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.CountedProduct;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class CountedProductTableModel extends GenericTableModel<CountedProduct> {

    public CountedProductTableModel(Collection<CountedProduct> data) {
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
                return NbBundle.getMessage(CountedProductTableModel.class, "TCN_Name");
            case 1:
                return NbBundle.getMessage(CountedProductTableModel.class, "TCN_Amount");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        CountedProduct c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getProduct().getName();
            case 1:
                return c.getAmount();
            default:
                return "";
        }
    }
}
