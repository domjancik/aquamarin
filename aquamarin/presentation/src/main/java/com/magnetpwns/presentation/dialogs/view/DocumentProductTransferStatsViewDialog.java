/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Document;
import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.TaxRate;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.Calendar;
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
public class DocumentProductTransferStatsViewDialog extends AbstractOkDialog {

    private int totalSum = 0;
    private Product product;
    
    private final Map<Integer, Integer> yearlyProductSums;
    
   public DocumentProductTransferStatsViewDialog(Collection<Document> documents, Product product) {        
        super("Přehled prodejů produktu " + product.getName());
        
        this.product = product;
        
        yearlyProductSums = new HashMap<Integer, Integer>();
        
        for (Document d : documents) {
            if (d instanceof Invoice) {
                Invoice i = (Invoice) d;

                if (!i.isCancelled()) {
                    totalSum += d.getProductTransferTotal();
                    
                    addTransfers(i);
                }
            }
        }
    }
   
    private void addTransfers(Invoice i) {
        Calendar c = Calendar.getInstance();
        c.setTime(i.getIssueDate());
        Integer year = c.get(Calendar.YEAR);
        
        Integer transfers = i.getProductTransferTotal();

        Integer oldValue = yearlyProductSums.getOrDefault(year, 0);

        yearlyProductSums.put(year, oldValue + transfers);
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        innerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel productNameLabel = new JLabel(product.getName());
        JLabel totalLabel = new JLabel("Celkem prodejů: " + totalSum);
        
        productNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(productNameLabel);
        
        innerPane.add(new JSeparator(JSeparator.HORIZONTAL));
        
        innerPane.add(totalLabel);
        
        innerPane.add(new JSeparator(JSeparator.HORIZONTAL));
        
        for (Integer yearKey : yearlyProductSums.keySet()) {
            String taxRateLabelString = yearKey.toString()+ ": ";
            taxRateLabelString += yearlyProductSums.get(yearKey);
            
            JLabel taxRateLabel = new JLabel(taxRateLabelString);
            taxRateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            innerPane.add(taxRateLabel);
        }
        
        return innerPane;
    }
}
