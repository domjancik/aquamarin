/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.DeliveryItem;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class DeliveryItemTableModel extends GenericTableModel<DeliveryItem> {

    public DeliveryItemTableModel(Collection<DeliveryItem> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(DeliveryItemTableModel.class, "TCN_Source");
            case 1:
                return NbBundle.getMessage(DeliveryItemTableModel.class, "TCN_Product");
            case 2:
                return NbBundle.getMessage(DeliveryItemTableModel.class, "TCN_Amount");
            case 3:
                return NbBundle.getMessage(DeliveryItemTableModel.class, "TCN_Blocked");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        DeliveryItem c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getSourceStock().toString();
            case 1:
                return c.getProduct().toString();
            case 2:
                return c.getAmount();
            case 3:
                return c.getBlocked();
            default:
                return "";
        }
    }
}
