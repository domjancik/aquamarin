/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author MagNet
 */
public class Document<T extends DocumentItem> implements Identifiable {
    
    private DocumentId id;
    private Client client;
    private Date issueDate;
    private Collection<T> items;
    private String note;
    private String deliveryNote;
    private int total;

    public Document(DocumentId id, Client client, Date issueDate, Collection<T> items, String note, String deliveryNote) {
        this.id = id;
        this.client = client;
        this.issueDate = issueDate;
        this.items = items;
        this.note = note;
        this.deliveryNote = deliveryNote;
        this.total = getDiscountedTaxedTotalRounded().intValue();
    }
    
    public Document(DocumentId id, Client client, Date issueDate, Collection<T> items, String note, String deliveryNote, int total) {
        this.id = id;
        this.client = client;
        this.issueDate = issueDate;
        this.items = items;
        this.note = note;
        this.deliveryNote = deliveryNote;
        this.total = total;
    }

    @Override
    public DocumentId getId() {
        return id;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (DocumentId) id;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Collection<T> getItems() {
        return items;
    }

    public Client getClient() {
        return client;
    }

    public String getNote() {
        return note;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }
    
    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DocumentItem t : items) {
            sum = sum.add(t.getTotal());
        }
        return sum;
    }
    
    public BigDecimal getTaxedTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DocumentItem t : items) {
            sum = sum.add(t.getTaxedTotal());
        }
        return sum;
    }
    
    public BigDecimal getDiscountedTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DocumentItem t : items) {
            sum = sum.add(t.getDiscountedTotal());
        }
        return sum;
    }
    
    public BigDecimal getDiscountedTaxedTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DocumentItem t : items) {
            sum = sum.add(t.getDiscountedTaxedTotal());
        }
        return sum;
    }
    
    public BigDecimal getDiscountedTaxedTotalRounded() {
        return getDiscountedTaxedTotal().setScale(0, RoundingMode.HALF_UP).setScale(2);
    }
    
    public BigDecimal getRoundingDifference() {
        return getDiscountedTaxedTotalRounded().subtract(getDiscountedTaxedTotal()).setScale(2);
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }
    
    public int getSysId() {
        return getId().getId();
    }

    /**
     * Get cached (not dynamically recalculated) total.
     * @return 
     */
    public int getSavedTotal() {
        return total;
    }
    
    
}

