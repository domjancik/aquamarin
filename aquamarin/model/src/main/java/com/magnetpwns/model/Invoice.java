/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * An invoice document, extends document by due date and the type of payment.
 * @author MagNet
 */
public class Invoice extends ProformaInvoice {
    
    private Collection<Payment> payments;
    private BigDecimal unpaid;
    private Date cancelledDate;
    private Date lastPaymentDate;
    private int seqNumber;

    public Invoice(DocumentId id, Client client, Date issueDate,
            Collection<DocumentItem> items, String note, String deliveryNote,
            Date endDate, PaymentType paymentType,
            Collection<Payment> payments, BigDecimal unpaid, Date cancelledDate, int seqNumber) {
        super(id, client, issueDate, items, note, deliveryNote, endDate, paymentType);
        this.payments = payments;
        if (unpaid == null)
            unpaid = BigDecimal.ZERO;
        this.unpaid = unpaid.setScale(2, RoundingMode.HALF_UP);
        this.cancelledDate = cancelledDate;
        if (this.cancelledDate == null) {
            this.cancelledDate = new Date(0);
        }
        this.seqNumber = seqNumber;
    }
    
    public Invoice(DocumentId id, Client client, Date issueDate,
            Collection<DocumentItem> items, String note, String deliveryNote,
            Integer total, Date endDate, PaymentType paymentType,
            Collection<Payment> payments, BigDecimal unpaid, Date cancelledDate, int seqNumber) {
        super(id, client, issueDate, items, note, deliveryNote, total, endDate, paymentType);
        this.payments = payments;
        if (unpaid == null)
            unpaid = BigDecimal.ZERO;
        this.unpaid = unpaid.setScale(2, RoundingMode.HALF_UP);
        this.cancelledDate = cancelledDate;
        if (this.cancelledDate == null) {
            this.cancelledDate = new Date(0);
        }
        this.seqNumber = seqNumber;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }
    
    public boolean isCancelled() {
        // TODO null date on uncancelled invoices
        
        Calendar c = Calendar.getInstance();
        c.setTime(cancelledDate);
        return c.get(Calendar.YEAR) > 1980;
    }

    public BigDecimal getUnpaid() {
        if (isCancelled()) {
            return BigDecimal.ZERO;
        }
        
        return unpaid;
    }

    public Collection<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }
    

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }
    
    public BigDecimal checkUnpaid() {
        BigDecimal setUnpaid = getDiscountedTaxedTotalRounded();
        if (payments != null) {
            for (Payment payment : payments) {
                setUnpaid = setUnpaid.subtract(payment.getAmount());
            }
        }
        unpaid = setUnpaid.setScale(2, RoundingMode.HALF_UP);
        return unpaid;
    }
    
    public void pay(Payment p) {
        payments.add(p);
        unpaid = checkUnpaid();
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    @Override
    public int getSysId() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getIssueDate());
        int sysId = cal.get(Calendar.YEAR) * 1000000;
        sysId += (cal.get(Calendar.MONTH) + 1) * 10000;
        sysId += getSeqNumber();
        
        return sysId;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }
}
