/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.model.Delivery;
import com.magnetpwns.presentation.model.DeliveryItemTableModel;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author MagNet
 */
public class DeliveryPreviewDialog extends AbstractViewDialog<Delivery> {

    public DeliveryPreviewDialog(Delivery document) {
        super("Náhled dodacího listu", document);
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
        
        innerPane.add(new JLabel("Opravdu si přejete vytvořit následující dodací list?"));
        
        innerPane.add(new JSeparator());
        
        innerPane.add(new JLabel("Odběratel: " + document.getClient().toString()));
        
        innerPane.add(initNoteView());
        innerPane.add(initIssueDateLabel());
        
        JTable itemTable = new JTable(new DeliveryItemTableModel(document.getItems()));
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(scrollPane);
        
        innerPane.add(initTotalLabel());
        
        return innerPane;
    }   

}
