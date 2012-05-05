/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.create;

import com.magnetpwns.format.Format;
import com.magnetpwns.model.Document;
import com.magnetpwns.model.PaymentType;
import com.magnetpwns.presentation.dialogs.AbstractDialog;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author MagNet
 */
public abstract class AbstractCreateDialog<T extends Document> extends AbstractDialog {
    
    protected JTextField noteField;
    protected JTextField deliveryNoteField;
    protected JXDatePicker issueDatePicker;
    protected JXDatePicker validDatePicker;
    protected JComboBox<PaymentType> paymentTypeComboBox;
    protected JLabel totalLabel;
    
    protected T document;

    public AbstractCreateDialog(String title) {
        super(title);
    }
    
    protected JPanel initPaymentTypeInput() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Způsob platby:"));
        panel.add(paymentTypeComboBox = new JComboBox<PaymentType>(PaymentType.values()));
        return panel;
    }
    
    protected JPanel initNoteInput() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Poznámka k dodání:"));
        panel.add(deliveryNoteField = new JTextField());
        deliveryNoteField.setColumns(100);
        topPanel.add(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Poznámka:"));
        panel.add(noteField = new JTextField());
        noteField.setColumns(100);
        topPanel.add(panel);
        
        return topPanel;
    }
    
    protected JPanel initDateInput() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Datum vydání:"));
        panel.add(issueDatePicker = new JXDatePicker(new Date()));
        
        return panel;
    }
    
    protected JPanel initTwoDateInput() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(new JLabel("Datum vydání:"));
        panel.add(issueDatePicker = new JXDatePicker(new Date()));
        
        panel.add(new JLabel("Datum platnosti:"));
        Calendar inAMonth = Calendar.getInstance();
        inAMonth.add(Calendar.DATE, 30);
        panel.add(validDatePicker = new JXDatePicker(inAMonth.getTime()));
        
        return panel;
    }
    
    protected void updateTotal() {
        totalLabel.setText("Celkem: " + document.getDiscountedTaxedTotalRounded().toPlainString());
    }
    
    protected JLabel initTotalLabel() {
        totalLabel = new JLabel();
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateTotal();
        return totalLabel;
    }
}
