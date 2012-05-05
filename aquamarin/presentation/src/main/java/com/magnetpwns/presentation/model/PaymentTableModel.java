/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Payment;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class PaymentTableModel extends GenericTableModel<Payment> {

    public PaymentTableModel(Collection<Payment> data) {
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
                return NbBundle.getMessage(PaymentTableModel.class, "TCN_Date");
            case 1:
                return NbBundle.getMessage(PaymentTableModel.class, "TCN_Amount");
            case 2:
                return NbBundle.getMessage(PaymentTableModel.class, "TCN_Log");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Payment p = items.get(i);
        
        switch (i1) {
            case 0:
                return p.getDate();
            case 1:
                return p.getAmount();
            case 2:
                return p.getLog();
            default:
                return "";
        }
    }
}
