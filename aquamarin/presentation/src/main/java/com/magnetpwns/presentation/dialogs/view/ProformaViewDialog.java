/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.model.*;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import com.magnetpwns.presentation.model.ProformaItemTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import javax.swing.*;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ProformaViewDialog extends AbstractOkDialog {

    private ProformaItemTableModel proformaItemTableModel;
    private JTable proformaItemTable;
    private ProformaInvoice proforma;
    
    private JLabel totalField;
    
    public ProformaViewDialog(ProformaInvoice proforma) {
        super(NbBundle.getMessage(ProformaViewDialog.class, "CTL_DialogViewProforma") + proforma.getClient().toString());
        this.proforma = proforma;
        proformaItemTableModel = new ProformaItemTableModel(proforma.getItems());
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        proformaItemTable = new JTable();
        proformaItemTable.setModel(proformaItemTableModel);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JButton printButton = new JButton();
        printButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/print24.png")));
        
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Print
            }
        });
        
        toolbar.add(printButton);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(toolbar);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Způsob platby:"));
        panel.add(new JLabel(proforma.getPaymentType().toString()));
        innerPane.add(panel);
        
        JLabel label = new JLabel("Poznámka k dodání:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel(proforma.getDeliveryNote());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel("Poznámka:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel(proforma.getNote());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
        //String dFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(proforma.getIssueDate());
        String dFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(proforma.getIssueDate());
        panel.add(new JLabel("Datum vydání: " + dFormat));
        
        dFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(proforma.getEndDate());
        panel.add(new JLabel("Datum platnosti: " + dFormat));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(panel);
        
        JScrollPane scrollPane = new JScrollPane(proformaItemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        totalField = new JLabel();
        totalField.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(totalField);
        
        updateTotal();
        
        return innerPane;
    }
    
    private void updateTotal() {
        totalField.setText("Součet: " + proforma.getTotal());
    }
    
}
