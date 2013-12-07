/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Document;
import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.TaxRate;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MagNet
 */
public class SumViewDialog extends AbstractOkDialog {

    private BigDecimal unpaidSum = BigDecimal.ZERO;
    private BigDecimal untaxedSum = BigDecimal.ZERO;
    private int totalSum = 0;
    
    private final Map<TaxRate, BigDecimal> taxRateSums;
    
   public SumViewDialog(Collection<Document> documents) {        
        super("Součet vybraných");
        
        taxRateSums = new HashMap<TaxRate, BigDecimal>();
        
        for (Document d : documents) {
            if (d instanceof Invoice) {    
                Invoice i = (Invoice) d;
                if (i.getItems() == null)
                    AquamarinFacade.getDefault().loadInvoice(i);
                if (!i.isCancelled()) {
                    totalSum += d.getSavedTotal();
                    unpaidSum = unpaidSum.add(((Invoice) d).getUnpaid());
                    untaxedSum = untaxedSum.add(d.getDiscountedTotal());
                    
                    addTaxRatesFromInvoice(i);
                }
            }
        }
    }
   
    private void addTaxRatesFromInvoice(Invoice i) {
        for (DocumentItem item : i.getItems()) {
            TaxRate taxRate = item.getProduct().getTaxRate();
            BigDecimal tax = item.getDiscountedTotalTax();
            
            if (!taxRateSums.containsKey(taxRate))
                taxRateSums.put(taxRate, BigDecimal.ZERO);
            
            BigDecimal oldValue = taxRateSums.get(taxRate);
            
            taxRateSums.put(taxRate, oldValue.add(tax));
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
        
        innerPane.add(new JSeparator(JSeparator.HORIZONTAL));
        
        for (TaxRate taxRateKey : taxRateSums.keySet()) {
            String taxRateLabelString = taxRateKey.toPercentString() + ": ";
            taxRateLabelString += taxRateSums.get(taxRateKey);
            
            JLabel taxRateLabel = new JLabel(taxRateLabelString);
            taxRateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            innerPane.add(taxRateLabel);
        }
        
        return innerPane;
    }
}
