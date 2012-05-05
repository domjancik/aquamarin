/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.StockTransfer;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class StockTransferTableModel extends GenericTableModel<StockTransfer> {

    public StockTransferTableModel(Collection<StockTransfer> data) {
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
                return NbBundle.getMessage(StockTransferTableModel.class, "TCN_Source");
            case 1:
                return NbBundle.getMessage(StockTransferTableModel.class, "TCN_Destination");
            case 2:
                return NbBundle.getMessage(StockTransferTableModel.class, "TCN_Date");
            case 3:
                return NbBundle.getMessage(StockTransferTableModel.class, "TCN_Type");
            case 4:
                return NbBundle.getMessage(StockTransferTableModel.class, "TCN_Note");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        StockTransfer s = items.get(i);
        
        switch (i1) {
            case 0:
                if (s.getSourceStock() == null)
                    return "";
                return s.getSourceStock().toString();
            case 1:
                return s.getDestinationStock().toString();
            case 2:
                return s.getDate();
            case 3:
                switch (s.getType()) {
                    case ADDREMOVE:
                        return NbBundle.getMessage(StockTransferTableModel.class, "CTL_TypeADDREMOVE");
                    case TRANSFER:
                        return NbBundle.getMessage(StockTransferTableModel.class, "CTL_TypeTRANSFER");
                    case SOLD:
                        return NbBundle.getMessage(StockTransferTableModel.class, "CTL_TypeSOLD");
                }
            case 4:
                return s.getNote();
            default:
                return "";
        }
    }
}
