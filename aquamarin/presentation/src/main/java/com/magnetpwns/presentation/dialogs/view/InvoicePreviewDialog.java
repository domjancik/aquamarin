/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.model.Invoice;
import com.magnetpwns.presentation.model.DocumentItemTableModel;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author MagNet
 */
public class InvoicePreviewDialog extends AbstractInvoiceViewDialog {

    public InvoicePreviewDialog(Invoice document) {
        super("Náhled faktury", document);
    }

    @Override
    protected void printAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        innerPane.setFont(innerPane.getFont().deriveFont(2));
        
        innerPane.add(new JLabel("Opravdu si přejete vytvořit následující fakturu?"));
        
        innerPane.add(new JSeparator());
        
        innerPane.add(new JLabel("Odběratel: " + document.getClient().toString()));
        
        innerPane.add(initNoteView());
        innerPane.add(initIssueDateLabel());
        innerPane.add(initEndDateLabel());
        
        JTable itemTable = new JTable(new DocumentItemTableModel(document.getItems()));
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(scrollPane);
        
        innerPane.add(initTotalLabel());
        
        return innerPane;
    }   
}
