/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.StockTransfer;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import com.magnetpwns.presentation.model.StockTransferTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author MagNet
 */
public class StockTransferHistoryDialog extends AbstractOkDialog {

    private StockTransferTableModel stockTransferTableModel;
    private JTable stockTransferTable;
    
    public StockTransferHistoryDialog() {
        super("Stock transfer history"); // TODO: Localize
        stockTransferTableModel = new StockTransferTableModel(AquamarinFacade.getDefault().findStockTransfers());
    }
    
    public StockTransferHistoryDialog(int year) {
        super("Stock transfer history - year " + Integer.toString(year)); // TODO: Localize
        stockTransferTableModel = new StockTransferTableModel(AquamarinFacade.getDefault().findStockTransfersByYear(year));
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        stockTransferTable = new JTable();
        stockTransferTable.setModel(stockTransferTableModel);
        
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
        
        toolbar.add(openButton);
        
        innerPane.add(toolbar);
        
        JScrollPane scrollPane = new JScrollPane(stockTransferTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }
    
}
