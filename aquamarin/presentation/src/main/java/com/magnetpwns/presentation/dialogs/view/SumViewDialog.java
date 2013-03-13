/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Document;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MagNet
 */
public class SumViewDialog extends AbstractOkDialog {

    private BigDecimal unpaidSum = BigDecimal.ZERO;
    private BigDecimal untaxedSum = BigDecimal.ZERO;
    private int totalSum = 0;
    
    public SumViewDialog(Collection<Document> documents) {
        super("Součet vybraných");
        
        for (Document d : documents) {
            if (d instanceof Invoice) {    
                Invoice i = (Invoice) d;
                if (i.getItems() == null)
                    AquamarinFacade.getDefault().loadInvoice(i);
                if (!i.isCancelled()) {
                    totalSum += d.getSavedTotal();
                    unpaidSum = unpaidSum.add(((Invoice) d).getUnpaid());
                    untaxedSum = untaxedSum.add(d.getDiscountedTotal());
                }
            }
        }
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        innerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel totalLabel = new JLabel("Celkem fakturováno: " + totalSum);
        JLabel untaxedLabel = new JLabel("(Bez DPH: " + untaxedSum + ")");
        JLabel unpaidLabel = new JLabel("Z toho nezaplaceno: " + unpaidSum.toPlainString());
        
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        untaxedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        unpaidLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(totalLabel);
        innerPane.add(untaxedLabel);
        innerPane.add(unpaidLabel);
        
        return innerPane;
    }
}
