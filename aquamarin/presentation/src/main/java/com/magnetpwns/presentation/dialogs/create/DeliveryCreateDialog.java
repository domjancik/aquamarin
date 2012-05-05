/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.create;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.StockTopComponent;
import com.magnetpwns.presentation.dialogs.DialogCommon;
import com.magnetpwns.presentation.dialogs.RequestDialog;
import com.magnetpwns.presentation.dialogs.view.DeliveryPreviewDialog;
import com.magnetpwns.presentation.model.StockItemTableModel;
import com.magnetpwns.presentation.model.TrackedProductTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class DeliveryCreateDialog extends AbstractCreateDialog<Delivery> {

    private TrackedProductTableModel trackedProductTableModel;
    private JTable trackedProductTable;
    private StockItemTableModel stockItemTableModel;
    private JTable stockItemTable;
    
    private Client client;
    private Stock sourceStock;
    private Stock favoriteStock = null;

    @Override
    protected void updateTotal() {
        Collection<TrackedProduct> items = trackedProductTableModel.getItems();
        BigDecimal sum = BigDecimal.ZERO;
        for (TrackedProduct trackedProduct : items) {
            sum = sum.add(trackedProduct.getTaxedTotal());
        }
        totalLabel.setText("Celkem: " + sum.toPlainString());
    }
    
    private void setFavoriteStock() {
        sourceStock = favoriteStock;
        stockItemTableModel.setItems(sourceStock.getItems());
    }
    
    public DeliveryCreateDialog(Client klient) {
        super(NbBundle.getMessage(AbstractCreateDialog.class, "CTL_DialogCreateDelivery") + klient.toString());
        this.client = klient;
        trackedProductTableModel = new TrackedProductTableModel(new ArrayList<TrackedProduct>());
        
        int favoriteStockOwnerId = NbPreferences.forModule(StockTopComponent.class).getInt("favoriteStockOwner", 0);
        if (favoriteStockOwnerId != 0) {
            Client favoriteStockOwner = AquamarinFacade.getDefault().findClient(new ClientId(favoriteStockOwnerId));
            favoriteStock = AquamarinFacade.getDefault().findStock(favoriteStockOwner);
        }
        
        getOkButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DeliveryItem> items = new ArrayList<DeliveryItem>();
                    for (TrackedProduct item : trackedProductTableModel.getItems()) {
                        items.add(new DeliveryItem(item.getProduct(), item.getAmount(), item.getSourceStock(), item.getAmount()));
                    }
                    Delivery d = new Delivery(
                            null,
                            client,
                            issueDatePicker.getDate(),
                            items,
                            noteField.getText(),
                            deliveryNoteField.getText(),
                            0);
                if (new DeliveryPreviewDialog(d).show()) {
                    AquamarinFacade.getDefault().addDelivery(d);
                    dispose();
                }
            }
        });
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        trackedProductTable = new JTable();
        trackedProductTable.setModel(trackedProductTableModel);
        stockItemTableModel = new StockItemTableModel(new ArrayList<StockItem>());
        stockItemTable = new JTable();
        TableRowSorter<StockItemTableModel> stockSorter = new TableRowSorter<StockItemTableModel>(stockItemTableModel);
        stockItemTable.setRowSorter(stockSorter);
        
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JButton stockSelectButton = new JButton();
        stockSelectButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/stockselect24.png")));
        
        stockSelectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Client sourceStockOwner = client;
                sourceStock = AquamarinFacade.getDefault().findStock(sourceStockOwner);
                stockItemTableModel.setItems(sourceStock.getItems());
            }
        });
        
        final JButton favoriteStockButton = new JButton();
        favoriteStockButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/favorite24.png")));
        
        favoriteStockButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {                               
                setFavoriteStock();
            }
        });
        
        final JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TrackedProduct selectedItem = getSelectedItem();
                    int amount = RequestDialog.showIntegerInputDialog();
                    while (amount > selectedItem.getAmount()) {
                        JOptionPane.showMessageDialog(null, "Na tomto dokumentu není dostatek zboží");
                        amount = RequestDialog.showIntegerInputDialog();
                    }
                    selectedItem.alterAmount(-amount);
                    trackedProductTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        trackedProductTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(!trackedProductTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        if (favoriteStock != null)
            toolbar.add(favoriteStockButton);
            setFavoriteStock();
        if (client.isStockEnabled())
            toolbar.add(stockSelectButton);
        toolbar.add(deleteButton);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(toolbar);
        
        innerPane.add(initNoteInput());
        innerPane.add(initDateInput());
        
        JPanel tablePane = new JPanel();
        tablePane.setLayout(new BoxLayout(tablePane, BoxLayout.LINE_AXIS));
        tablePane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(trackedProductTable);
        tablePane.add(scrollPane);
        
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOrientation(JToolBar.VERTICAL);
        
        final JButton transferLeftButton = new JButton();
        transferLeftButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/transferleft24.png")));
        transferLeftButton.setEnabled(false);
        
        transferLeftButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StockItem selected = getSelectedStockItem();
                    TrackedProduct tp = findItem(selected);
                    int amount;
                    do {
                        amount = RequestDialog.showIntegerInputDialog();
                        if (tp != null) {
                            amount += tp.getAmount();
                        }
                    } while (amount > selected.getAvailableAmount());
                    
                    trackedProductTableModel.alter(
                            new TrackedProduct(selected.getProduct(), amount, sourceStock.getOwner()));
                    updateTotal();
                } catch (CancelException ex) {
                }
            }
        });
                
        stockItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                transferLeftButton.setEnabled(!stockItemTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(transferLeftButton);
        tablePane.add(toolbar);
        
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(stockItemTable);
        
        rightPane.add(DialogCommon.initProductFilterPanel(stockSorter, 1));
        rightPane.add(scrollPane);
        
        tablePane.add(rightPane);
        
        innerPane.add(tablePane);
        
        innerPane.add(initTotalLabel());
        
        return innerPane;
    }

    private TrackedProduct findItem(StockItem si) {
        Collection<TrackedProduct> items = trackedProductTableModel.getItems();
        for (TrackedProduct trackedProduct : items) {
            if (trackedProduct.getSourceStock().equals(sourceStock.getOwner()) && trackedProduct.getProduct().equals(si.getProduct()))
                return trackedProduct;
        }
        return null;
    }
    
    private TrackedProduct getSelectedItem() {
        return trackedProductTableModel.get(trackedProductTable.convertRowIndexToModel(trackedProductTable.getSelectedRow()));
    }
    
    private StockItem getSelectedStockItem() {
        return stockItemTableModel.get(stockItemTable.convertRowIndexToModel(stockItemTable.getSelectedRow()));
    }    
}
