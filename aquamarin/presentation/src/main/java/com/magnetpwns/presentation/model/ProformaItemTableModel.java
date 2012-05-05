/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.StockItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ProformaItemTableModel extends GenericTableModel<DocumentItem> {

    public ProformaItemTableModel(Collection<DocumentItem> data) {
        super(data);
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_Product");
            case 1:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_Amount");
            case 2:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_Price");
            case 3:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_Discount");
            case 4:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_TaxRate");
            case 5:
                return NbBundle.getMessage(ProformaItemTableModel.class, "TCN_Total");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        DocumentItem c = items.get(i);
        
        switch (i1) {
            case 0:
                return c.getProduct().toString();
            case 1:
                return c.getAmount();
            case 2:
                return c.getProduct().getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString();
            case 3:
                return c.getDiscount().setScale(2, RoundingMode.HALF_UP).toPlainString();
            case 4:
                return c.getProduct().getTaxRate().toPercentString();
            case 5:
                return c.getTotal().setScale(2, RoundingMode.HALF_UP).toPlainString();
            default:
                return "";
        }
    }
    
    public void alter(CountedProduct cp) {
        for (CountedProduct countedProduct : items) {
            if (cp.getProduct().equals(countedProduct.getProduct())) {
                countedProduct.alterAmount(cp.getAmount());
                fireTableDataChanged();
                return;
            }
        }
        add(new DocumentItem(cp.getProduct(), cp.getAmount(), BigDecimal.ZERO, null));
    }
}
