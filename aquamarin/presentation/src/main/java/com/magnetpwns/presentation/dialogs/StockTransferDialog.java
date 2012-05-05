/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.ProductId;
import com.magnetpwns.model.Stock;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.model.StockItemTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author MagNet
 */
public class StockTransferDialog extends AbstractDialog {

    private StockItemTableModel stockItemTableModelLeft;
    private JTable stockItemTableLeft;
    private StockItemTableModel stockItemTableModelRight;
    private JTable stockItemTableRight;
    private Stock stockLeft;
    private Stock stockRight;
    
    private Map<ProductId, CountedProduct> itemChanges;
    
    public StockTransferDialog(Stock stockLeft, Stock stockRight) {
        super(stockLeft.getOwner().toString() + " - " + stockRight.getOwner().toString()  + " - Stock transfer"); // TODO: Localize
        this.stockLeft = stockLeft;
        this.stockRight = stockRight;
        stockItemTableModelLeft = new StockItemTableModel(stockLeft.getItems());
        stockItemTableModelRight = new StockItemTableModel(stockRight.getItems());
        itemChanges = new HashMap<ProductId, CountedProduct>();
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.LINE_AXIS));
        
        stockItemTableLeft = new JTable();
        stockItemTableLeft.setModel(stockItemTableModelLeft);
        stockItemTableLeft.setAutoCreateRowSorter(true);
        stockItemTableRight = new JTable();
        stockItemTableRight.setModel(stockItemTableModelRight);
        stockItemTableRight.setAutoCreateRowSorter(true);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setOrientation(JToolBar.VERTICAL);
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JButton transferLeftButton = new JButton();
        transferLeftButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/transferleft24.png")));
        transferLeftButton.setEnabled(false);
        
        transferLeftButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                beginTransfer(true);
            }
        });
        
        final JButton transferRightButton = new JButton();
        transferRightButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/transferright24.png")));
        transferRightButton.setEnabled(false);
        
        transferRightButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                beginTransfer(false);
            }
        });
        
        stockItemTableLeft.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                transferRightButton.setEnabled(!stockItemTableLeft.getSelectionModel().isSelectionEmpty());
            }
        });
        
        stockItemTableRight.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                transferLeftButton.setEnabled(!stockItemTableRight.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(transferRightButton);
        toolbar.add(transferLeftButton);
        
        JScrollPane scrollPane = new JScrollPane(stockItemTableLeft);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        innerPane.add(toolbar);
        
        scrollPane = new JScrollPane(stockItemTableRight);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        return innerPane;
    }

    private void addChange(CountedProduct change) {
        CountedProduct oldChange = itemChanges.put(change.getProduct().getId(), change);
        if (oldChange != null) {
            change.alterAmount(oldChange.getAmount());
        }
    }
    
    /**
     * 
     * @param p
     * @param amount
     * @param side direction: false = right, true = left
     */
    private void completeTransfer(Product p, int amount, boolean side) {
        CountedProduct change = new CountedProduct(p, amount);
        if (!side) {
            stockItemTableModelLeft.alter(change.getInverse());
            stockItemTableModelRight.alter(change);
            addChange(change);
        } else {
            stockItemTableModelRight.alter(change.getInverse());
            stockItemTableModelLeft.alter(change);
            addChange(change.getInverse());
        }
    }
    
    private void beginTransfer(boolean side) {
        try {
            CountedProduct selectedItem;
            if (!side) {
                selectedItem = stockItemTableModelLeft.get(stockItemTableLeft.convertRowIndexToModel(stockItemTableLeft.getSelectedRow()));
            } else {
                selectedItem = stockItemTableModelRight.get(stockItemTableRight.convertRowIndexToModel(stockItemTableRight.getSelectedRow()));
            }
            int amount = requestAmount();
            while (amount > selectedItem.getAmount()) {
                JOptionPane.showMessageDialog(null, "Na tomto skladě není dostatek zboží");
                amount = requestAmount();
            }
            Product selectedProduct = selectedItem.getProduct();
            completeTransfer(selectedProduct, amount, side);
        } catch (CancelException ex) {
            return;
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
    
    private Date showForcedDateInputDialog() {
        try {
            return RequestDialog.showDateInputDialog();
        } catch (CancelException ex) {
            return showForcedDateInputDialog();
        }
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    Date date = showForcedDateInputDialog();
                    String input = JOptionPane.showInputDialog("Poznámka k akci");
                    if (input == null)
                        input = "";
                    AquamarinFacade.getDefault().transferItems(stockLeft,
                            stockRight, itemChanges.values(), input, date);
                    result = true;
                }
            }
        };
    }
    
}
