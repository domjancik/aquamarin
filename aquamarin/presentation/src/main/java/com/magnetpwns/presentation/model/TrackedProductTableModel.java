/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.TrackedProduct;
import com.magnetpwns.model.TrackedProduct;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class TrackedProductTableModel extends GenericTableModel<TrackedProduct> {

    public TrackedProductTableModel(Collection<TrackedProduct> data) {
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
                return NbBundle.getMessage(TrackedProductTableModel.class, "TCN_Source");
            case 1:
                return NbBundle.getMessage(TrackedProductTableModel.class, "TCN_Product");
            case 2:
                return NbBundle.getMessage(TrackedProductTableModel.class, "TCN_Amount");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        TrackedProduct c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getSourceStock().toString();
            case 1:
                return c.getProduct().toString();
            case 2:
                return c.getAmount();
            default:
                return "";
        }
    }
    
    public void alter(TrackedProduct cp) {
        for (CountedProduct countedProduct : items) {
            if (cp.getProduct().equals(countedProduct.getProduct())) {
                countedProduct.alterAmount(cp.getAmount());
                fireTableDataChanged();
                return;
            }
        }
        add(new TrackedProduct(cp.getProduct(), cp.getAmount(), cp.getSourceStock()));
    }
}
