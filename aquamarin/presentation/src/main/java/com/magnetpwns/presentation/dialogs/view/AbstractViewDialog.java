/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.format.Format;
import com.magnetpwns.model.Document;
import com.magnetpwns.presentation.dialogs.AbstractDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author MagNet
 */
public abstract class AbstractViewDialog<T extends Document> extends AbstractDialog {
    
    protected T document;
    
    protected JLabel totalLabel;

    public AbstractViewDialog(String title, T document) {
        super(title);
        this.document = document;
    }
    
    protected JPanel initNoteView() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        
        line.add(new JLabel("Poznámka k dodání:"));
        line.add(new JLabel(document.getDeliveryNote()));
        
        panel.add(line);
        line = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        
        line.add(new JLabel("Poznámka:"));
        line.add(new JLabel(document.getNote()));
        
        panel.add(line);
        
        return panel;
    }
    
    protected void updateTotal() {
        totalLabel.setText("Celkem: " + document.getDiscountedTaxedTotalRounded().toPlainString());
    }
    
    protected JLabel initTotalLabel() {
        totalLabel = new JLabel();
        updateTotal();
        totalLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return totalLabel;
    }
    
    protected JButton initPrintButton() {
        JButton printButton = new JButton();
        printButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/print24.png")));
        
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printAction();
            }
        });
        
        return printButton;
    }
    
    protected JLabel createLeftAlignedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return label;
    }
    
    private JPanel createFlowPanel(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(component);
        return panel;
    }
    
    protected JPanel initIssueDateLabel() {
        return createFlowPanel(
            createLeftAlignedLabel("Datum vydání: " + Format.DATE_FORMAT.format(document.getIssueDate())));
    }
    
    protected abstract void printAction();
}
