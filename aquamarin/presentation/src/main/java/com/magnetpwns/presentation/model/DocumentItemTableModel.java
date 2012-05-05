/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.TrackedProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class DocumentItemTableModel extends GenericTableModel<DocumentItem> {

    public DocumentItemTableModel(Collection<DocumentItem> data) {
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
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Source");
            case 1:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Product");
            case 2:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Amount");
            case 3:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Price");
            case 4:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Discount");
            case 5:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_TaxRate");
            case 6:
                return NbBundle.getMessage(DocumentItemTableModel.class, "TCN_Total");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        DocumentItem c = items.get(i);
        
        switch (i1) {
            case 0:
                if (c.getSourceStock() != null)
                    return c.getSourceStock().toString();
                return "";
            case 1:
                return c.getProduct().toString();
            case 2:
                return c.getAmount();
            case 3:
                return c.getDiscountedTaxedProductPrice();
            case 4:
                return c.getDiscount();
            case 5:
                return c.getProduct().getTaxRate().toPercentString();
            case 6:
                return c.getDiscountedTaxedTotal();
            default:
                return "";
        }
    }
    
    public void alter(TrackedProduct tp) {
        for (TrackedProduct trackedProduct : items) {
            if (tp.getProduct().equals(trackedProduct.getProduct()) && tp.getSourceStock().equals(trackedProduct.getSourceStock())) {
                trackedProduct.alterAmount(tp.getAmount());
                fireTableDataChanged();
                return;
            }
        }
        add(new DocumentItem(tp.getProduct(), tp.getAmount(), BigDecimal.ZERO, tp.getSourceStock()));
    }
    
    
}
