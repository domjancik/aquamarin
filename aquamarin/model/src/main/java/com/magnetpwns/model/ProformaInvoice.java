/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public class ProformaInvoice extends Document<DocumentItem> {

    Date endDate;
    private PaymentType paymentType;

    public ProformaInvoice(DocumentId id, Client client, Date issueDate,
            Collection<DocumentItem> items, String note, String deliveryNote,
            Date endDate, PaymentType paymentType) {
        super(id, client, issueDate, items, note, deliveryNote);
        this.endDate = endDate;
        this.paymentType = paymentType;
    }
    
    public ProformaInvoice(DocumentId id, Client client, Date issueDate,
            Collection<DocumentItem> items, String note, String deliveryNote,
            Integer total, Date endDate, PaymentType paymentType) {
        super(id, client, issueDate, items, note, deliveryNote, total);
        this.endDate = endDate;
        this.paymentType = paymentType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
