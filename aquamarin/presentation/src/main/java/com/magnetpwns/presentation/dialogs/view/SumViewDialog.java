/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

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
    private int totalSum = 0;
    
    public SumViewDialog(Collection<Document> documents) {
        super("Součet vybraných");
        
        for (Document d : documents) {
            totalSum += d.getSavedTotal();
            if (d instanceof Invoice) {
                unpaidSum = unpaidSum.add(((Invoice) d).getUnpaid());
            }
        }
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        innerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel totalLabel = new JLabel("Celkem fakturováno: " + totalSum);
        JLabel unpaidLabel = new JLabel("Z toho nezaplaceno: " + unpaidSum.toPlainString());
        
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        unpaidLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(totalLabel);
        innerPane.add(unpaidLabel);
        
        return innerPane;
    }
}
