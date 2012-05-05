/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.StockItem;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class StockItemTableModel extends GenericTableModel<StockItem> {

    public StockItemTableModel(Collection<StockItem> data) {
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
                return NbBundle.getMessage(StockItemTableModel.class, "TCN_Code");
            case 1:
                return NbBundle.getMessage(StockItemTableModel.class, "TCN_Name");
            case 2:
                return NbBundle.getMessage(StockItemTableModel.class, "TCN_Amount");
            case 3:
                return NbBundle.getMessage(StockItemTableModel.class, "TCN_AmountMin");
            case 4:
                return NbBundle.getMessage(StockItemTableModel.class, "TCN_Blocked");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        StockItem s = items.get(i);
        
        switch (i1) {
            case 0:
                return s.getProduct().getCode();
            case 1:
                return s.getProduct().toString();
            case 2:
                return s.getAmount();
            case 3:
                return s.getMinimalAmount();
            case 4:
                return s.getBlockedAmount();
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
        add(new StockItem(cp.getProduct(), cp.getAmount(), 0, 0));
    }
    
    public void setMinimum(StockItem si) {
        for (StockItem stockItem : items) {
            if (stockItem.equals(si))
                return;
        }
    }

    @Override
    public void setItems(Collection<StockItem> items) {
        super.setItems(new ArrayList<StockItem>(items));
    }
    
    
}
