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
public class Delivery extends Document<DeliveryItem> {

    private int blocking;
    
    public Delivery(DocumentId id, Client client, Date issueDate,
            Collection<DeliveryItem> items, String note, String deliveryNote,
            int blocking) {
        super(id, client, issueDate, items, note, deliveryNote);
        this.blocking = blocking;
    }
    
    public Delivery(DocumentId id, Client client, Date issueDate,
            Collection<DeliveryItem> items, String note, String deliveryNote,
            Integer total, int blocking) {
        super(id, client, issueDate, items, note, deliveryNote, total);
        this.blocking = blocking;
    }

    public int getBlocking() {
        return blocking;
    }

    public int checkBlocking() {
        int blockingSum = 0;
        for (DeliveryItem item : getItems()) {
            blockingSum += item.getBlocked();
        }
        blocking = blockingSum;
        return blockingSum;
    }
}
