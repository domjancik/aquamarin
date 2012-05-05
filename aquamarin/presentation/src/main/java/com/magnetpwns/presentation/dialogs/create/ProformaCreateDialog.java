/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.create;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.dialogs.PriceSelectionDialog;
import com.magnetpwns.presentation.dialogs.ProductSelectionDialog;
import com.magnetpwns.presentation.dialogs.RequestDialog;
import com.magnetpwns.presentation.dialogs.view.ProformaPreviewDialog;
import com.magnetpwns.presentation.model.ProformaItemTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ProformaCreateDialog extends AbstractCreateDialog<ProformaInvoice> {

    private ProformaItemTableModel proformaItemTableModel;
    private JTable proformaItemTable;
    private Client client;
    
    public ProformaCreateDialog(Client klient) {
        super(NbBundle.getMessage(ProformaCreateDialog.class, "CTL_DialogCreateProforma") + klient.toString());
        this.client = klient;
        proformaItemTableModel = new ProformaItemTableModel(new ArrayList<DocumentItem>());
        document = new ProformaInvoice(null, klient, null, proformaItemTableModel.getItems(), null, null, null, null);
        
        getOkButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ProformaInvoice pi = new ProformaInvoice(
                            null,
                            client,
                            issueDatePicker.getDate(),
                            proformaItemTableModel.getItems(),
                            noteField.getText(),
                            deliveryNoteField.getText(),
                            validDatePicker.getDate(),
                            (PaymentType) paymentTypeComboBox.getSelectedItem());
                if (new ProformaPreviewDialog(pi).show()) {
                    AquamarinFacade.getDefault().addProforma(pi);
                    dispose();
                }
            }
        });
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
        
        final JButton addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/add24.png")));
        
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product selectedProduct = new ProductSelectionDialog().getSelection();
                    try {
                        new PriceSelectionDialog(selectedProduct).show();
                    } catch (AquamarinException ex) {}
                    int amount = RequestDialog.showIntegerInputDialog();
                    CountedProduct change = new CountedProduct(selectedProduct, amount);
                    proformaItemTableModel.alter(change);
                    updateTotal();
                } catch (CancelException ex) {
                    // Action was cancelled, disregard.
                }
            }
        });
        
        final JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DocumentItem selectedItem = getSelectedItem();
                    int amount = RequestDialog.showIntegerInputDialog();
                    while (amount > selectedItem.getAmount()) {
                        JOptionPane.showMessageDialog(null, "Na tomto dokumentu není dostatek zboží");
                        amount = RequestDialog.showIntegerInputDialog();
                    }
                    selectedItem.alterAmount(-amount);
                    proformaItemTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        final JButton setDiscountButton = new JButton();
        setDiscountButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/discount24.png")));
        setDiscountButton.setEnabled(false);
        
        setDiscountButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BigDecimal discount = RequestDialog.showBigDecimalInputDialog();
                    DocumentItem selectedItem = getSelectedItem();
                    selectedItem.setDiscount(discount);
                    proformaItemTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        proformaItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(!proformaItemTable.getSelectionModel().isSelectionEmpty());
                setDiscountButton.setEnabled(!proformaItemTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(addButton);
        toolbar.add(deleteButton);
        toolbar.add(setDiscountButton);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(toolbar);
        
        innerPane.add(initPaymentTypeInput());
        innerPane.add(initNoteInput());
        innerPane.add(initTwoDateInput());
        
        JScrollPane scrollPane = new JScrollPane(proformaItemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        innerPane.add(initTotalLabel());
        
        return innerPane;
    }

    private DocumentItem getSelectedItem() {
        return proformaItemTableModel.get(proformaItemTable.convertRowIndexToModel(proformaItemTable.getSelectedRow()));
    }    
}
