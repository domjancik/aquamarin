/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Delivery;
import com.magnetpwns.model.DeliveryItem;
import com.magnetpwns.model.TrackedProduct;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.dialogs.AbstractDialog;
import com.magnetpwns.presentation.dialogs.RequestDialog;
import com.magnetpwns.presentation.model.DeliveryItemTableModel;
import com.magnetpwns.printing.DeliveryPrint;
import com.magnetpwns.printing.Print;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class DeliveryViewDialog extends AbstractDialog {

    private DeliveryItemTableModel deliveryItemTableModel;
    private JTable deliveryItemTable;
    
    private JLabel totalField;
    
    private Delivery delivery;
    
    private ArrayList<TrackedProduct> blockChanges;
    
    public DeliveryViewDialog(Delivery delivery) {
        super(NbBundle.getMessage(DeliveryViewDialog.class, "CTL_DialogViewDelivery") + delivery.getClient().toString());
        this.delivery = delivery;
        deliveryItemTableModel = new DeliveryItemTableModel(delivery.getItems());
        blockChanges = new ArrayList<TrackedProduct>();
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        deliveryItemTable = new JTable();
        deliveryItemTable.setModel(deliveryItemTableModel);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton sellButton = new JButton();
        sellButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/invoice24.png")));
        // TODO: Delivery->Invoice dialog
        
        final JButton printButton = new JButton();
        printButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/print24.png")));
        
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Print.print(new DeliveryPrint(delivery));
            }
        });
        
        final JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DeliveryItem selectedItem = getSelectedItem();
                    int amount = RequestDialog.showIntegerInputDialog();
                    while (amount > selectedItem.getBlocked()) {
                        JOptionPane.showMessageDialog(null, "Na tomto dokumentu není dostatek zboží");
                        amount = RequestDialog.showIntegerInputDialog();
                    }
                    selectedItem.alterBlocks(-amount);
                    blockChanges.add(
                            new TrackedProduct(selectedItem.getProduct(), -amount, selectedItem.getSourceStock()));
                    deliveryItemTableModel.fireTableDataChanged();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        deliveryItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(!deliveryItemTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(printButton);
        toolbar.add(deleteButton);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(toolbar);
        
        JLabel label = new JLabel("Poznámka k dodání:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel(delivery.getDeliveryNote());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel("Poznámka:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        label = new JLabel(delivery.getNote());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(label);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
        panel.add(new JLabel("Datum vydání:"));
        //panel.add(issueDateSpinner = new JSpinner(new SpinnerDateModel()));
        //issueDateSpinner.setEditor(new JSpinner.DateEditor(issueDateSpinner, Format.DATE));
        
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(panel);
        
        JScrollPane scrollPane = new JScrollPane(deliveryItemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        totalField = new JLabel();
        totalField.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(totalField);
        
        return innerPane;
    }
    
    private DeliveryItem getSelectedItem() {
        return deliveryItemTableModel.get(deliveryItemTable.convertRowIndexToModel(deliveryItemTable.getSelectedRow()));
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    AquamarinFacade.getDefault().alterDeliveryBlocks(delivery, blockChanges);
                    result = true;
                }
            }
        };
    }
    
}
