/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.StockTransfer;
import com.magnetpwns.model.exception.NoResultException;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import com.magnetpwns.presentation.dialogs.MessageDialog;
import com.magnetpwns.presentation.model.StockTransferTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class StockTransferHistoryDialog extends AbstractOkDialog {

    private StockTransferTableModel stockTransferTableModel;
    private JTable stockTransferTable;
    
    private int year;
    
    private JComboBox<Product> productComboBox;
    
//    public StockTransferHistoryDialog() {
//        super(NbBundle.getMessage(StockTransferHistoryDialog.class, "CTL_StockTransferHistory"));
//        stockTransferTableModel = new StockTransferTableModel(AquamarinFacade.getDefault().findStockTransfers());
//    }
//    
    public StockTransferHistoryDialog(int year) {
        super(NbBundle.getMessage(StockTransferHistoryDialog.class, "CTL_StockTransferHistoryYear") + year);
        this.year = year;
        stockTransferTableModel = new StockTransferTableModel(AquamarinFacade.getDefault().findStockTransfersByYear(year));
    }
    
    private void loadYear() {
        stockTransferTableModel.setItems(AquamarinFacade.getDefault().findStockTransfersByYear(year));
    }
    
    private void loadYearProduct() throws NoResultException {
        Product p = (Product) productComboBox.getSelectedItem();
        stockTransferTableModel.setItems(AquamarinFacade.getDefault().findStockTransfersByYearProduct(year, p));
    }
    
    private void updateTableModel() {
        stockTransferTable.setModel(stockTransferTableModel);
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        stockTransferTable = new JTable();
        stockTransferTable.setAutoCreateRowSorter(true);
        updateTableModel();
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JButton openButton = new JButton();
        openButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/list24.png")));
        openButton.setEnabled(false);
        
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // View the transfer contents
                StockTransfer selectedItem = stockTransferTableModel.get(stockTransferTable.convertRowIndexToModel(stockTransferTable.getSelectedRow()));
                new StockTransferHistoryOpenDialog(AquamarinFacade.getDefault().loadStockTransfer(selectedItem)).show();
            }
        });
        
        stockTransferTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                openButton.setEnabled(!stockTransferTable.getSelectionModel().isSelectionEmpty());
            }
        });
       
        productComboBox = new JComboBox(AquamarinFacade.getDefault().findAllProducts().toArray());
        
        toolbar.add(openButton);
        toolbar.add(productComboBox);
        
        final JButton filterButton = new JButton();
        filterButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/filter24.png")));
        filterButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadYearProduct();
                    updateTableModel();
                } catch (NoResultException ex) {
                    MessageDialog.showNoResultAlertDialog();
                }
            }
        });
        
        final JButton filterClearButton = new JButton();
        filterClearButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/filterclear24.png")));
        filterClearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadYear();
                updateTableModel();
            }
        });
        
        toolbar.add(filterButton);
        toolbar.add(filterClearButton);
        
        innerPane.add(toolbar);
        
        JScrollPane scrollPane = new JScrollPane(stockTransferTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }
    
}
