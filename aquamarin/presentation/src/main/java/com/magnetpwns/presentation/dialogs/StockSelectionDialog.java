/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Client;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.model.AddressTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author MagNet
 */
public class StockSelectionDialog extends AbstractDialog {

    private AddressTableModel stockTableModel;
    private JTable stockTable;
    
    public StockSelectionDialog() {
        super("Select stock");
        stockTableModel = new AddressTableModel(AquamarinFacade.getDefault().findStockOwners());
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        stockTable = new JTable();
        stockTable.setModel(stockTableModel);
        
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }
    
    public Client getSelection() throws CancelException {
        if (show() == true) {
            return stockTableModel.get(stockTable.convertRowIndexToModel(stockTable.getSelectedRow()));
        } else {
            throw new CancelException();
        }
    }

    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    result = true;
                }
            }
        };
    }
    
}
