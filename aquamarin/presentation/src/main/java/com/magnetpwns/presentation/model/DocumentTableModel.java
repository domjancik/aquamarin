/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Delivery;
import com.magnetpwns.model.Document;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.ProformaInvoice;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class DocumentTableModel extends GenericTableModel<Document> {
    
    public DocumentTableModel(Collection<Document> data) {
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
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Id");
            case 1:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Client");
            case 2:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Date");
            case 3:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Type");            
            case 4:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Left");            
            case 5:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_Total");
            case 6:
                return NbBundle.getMessage(DocumentTableModel.class, "TCN_PaidOn");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Document d = items.get(i);
        
        String ret;
        
        switch (i1) {
            case 0:
                return d.getSysId();
            case 1:
                return d.getClient().toString();
            case 2:
                return d.getIssueDate();
            case 3:
                if (d instanceof Invoice) {
                    ret = NbBundle.getMessage(DocumentTableModel.class, "CTL_DocumentInvoice");
                    if (((Invoice) d).isCancelled()) {
                        ret += " (";
                        ret += NbBundle.getMessage(DocumentTableModel.class, "CTL_Cancelled");
                        ret += ")";
                    }
                    return ret;
                }
                if (d instanceof ProformaInvoice)
                    return NbBundle.getMessage(DocumentTableModel.class, "CTL_DocumentProforma");
                if (d instanceof Delivery)
                    return NbBundle.getMessage(DocumentTableModel.class, "CTL_DocumentDelivery");
            case 4:
                if (d instanceof Invoice)
                    return ((Invoice) d).getUnpaid().setScale(0, RoundingMode.HALF_UP).intValue();
                if (d instanceof ProformaInvoice)
                    return 0;
                if (d instanceof Delivery)
                    return ((Delivery) d).getBlocking();
            case 5:
                return d.getSavedTotal();
            case 6:
                if (d instanceof Invoice)
                    return ((Invoice)d).getLastPaymentDate();
            default:
                return "";
        }
    }

    @Override
    public Class getColumnClass(int c) {
        if (c == 6)
            return Date.class;
        
        return super.getColumnClass(c);
    }
}
