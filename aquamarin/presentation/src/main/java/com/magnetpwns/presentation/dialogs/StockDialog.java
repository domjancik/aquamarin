/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.dialogs.edit.ProductEditDialog;
import com.magnetpwns.presentation.model.StockItemTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class StockDialog extends AbstractDialog {

    private StockItemTableModel stockItemTableModel;
    private JTable stockItemTable;
    private Stock stock;
    
    private Map<ProductId, CountedProduct> itemChanges;
    
    public StockDialog(Stock stock) {
        super (stock.getOwner().toString() + " - Stock"); // TODO: Localize
        this.stock = stock;
        stockItemTableModel = new StockItemTableModel(stock.getItems());
        itemChanges = new HashMap<ProductId, CountedProduct>();
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        stockItemTable = new JTable();
        stockItemTable.setModel(stockItemTableModel);
        stockItemTable.setAutoCreateRowSorter(true);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton productEditButton = new JButton();
        productEditButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/edit24.png")));
        
        productEditButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductEditDialog().show();
            }
        });
        
        final JButton addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/add24.png")));
        
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product selectedProduct = new ProductSelectionDialog().getSelection();
                    int amount = requestAmount();
                    CountedProduct change = new CountedProduct(selectedProduct, amount);
                    applyChange(selectedProduct, amount);
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
                    StockItem selectedItem = stockItemTableModel.get(stockItemTable.convertRowIndexToModel(stockItemTable.getSelectedRow()));
                    int amount = requestAmount();
                    while (amount > selectedItem.getAmount()) {
                        JOptionPane.showMessageDialog(null, "Na tomto skladě není dostatek zboží");
                        amount = requestAmount();
                    }
                    Product selectedProduct = selectedItem.getProduct();
                    applyChange(selectedProduct, -amount);
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        final JButton setMinButton = new JButton();
        setMinButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/setmin24.png")));
        setMinButton.setEnabled(false);
        
        setMinButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int minAmount = requestAmount();
                    StockItem selectedItem = stockItemTableModel.get(stockItemTable.convertRowIndexToModel(stockItemTable.getSelectedRow()));
                    StockItem updatedItem = new StockItem(selectedItem.getProduct(), selectedItem.getAmount(), minAmount, selectedItem.getBlockedAmount());
                    if (AquamarinFacade.getDefault().setStockItemMinimalCount(stock, updatedItem)) {
                        selectedItem.setMinimalAmount(minAmount);
                        stockItemTableModel.fireTableDataChanged();
                    }
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        stockItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(!stockItemTable.getSelectionModel().isSelectionEmpty());
                setMinButton.setEnabled(!stockItemTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(productEditButton);
        toolbar.add(addButton);
        toolbar.add(deleteButton);
        toolbar.add(setMinButton);
        
        innerPane.add(toolbar);
        
        JScrollPane scrollPane = new JScrollPane(stockItemTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }

    private void applyChange(Product p, int amount) {
        CountedProduct change = new CountedProduct(p, amount);
        stockItemTableModel.alter(change);
        CountedProduct oldChange = itemChanges.put(p.getId(), change);
        if (oldChange != null) {
            change.alterAmount(oldChange.getAmount());
        }
    }
    
    private int requestAmount() throws CancelException {
        try {
            String result = JOptionPane.showInputDialog("Kolik?");
            if (result == null)
                throw new CancelException();
            int amount = Integer.parseInt(result);
            if (amount > 0) {
                return amount;
            }
        } catch (NumberFormatException e) {
            // Disregard
        }
        JOptionPane.showMessageDialog(null, "Použijte prosím celé nezáporné číslo");
        return requestAmount();
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    String input = JOptionPane.showInputDialog("Poznámka k akci");
                    if (input == null)
                        input = "";
                    try {
                        AquamarinFacade.getDefault().alterItems(stock, itemChanges.values(), input);
                    } catch (AquamarinException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    result = true;
                }
            }
        };
    }
    
}
