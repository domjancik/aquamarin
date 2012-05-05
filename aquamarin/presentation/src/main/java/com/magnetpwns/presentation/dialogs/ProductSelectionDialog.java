/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.CountedProduct;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.model.ProductTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author MagNet
 */
public class ProductSelectionDialog extends AbstractDialog {

    private ProductTableModel productTableModel;
    private JTable productTable;
    
    public ProductSelectionDialog() {
        super("Select products");
        productTableModel = new ProductTableModel(AquamarinFacade.getDefault().findAllProducts());
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        productTable = new JTable();
        productTable.setModel(productTableModel);
        TableRowSorter<ProductTableModel> sorter = new TableRowSorter<ProductTableModel>(productTableModel);
        productTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panel = DialogCommon.initProductFilterPanel(sorter, 2);
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        innerPane.add(panel);
        innerPane.add(scrollPane);
        
        return innerPane;
    }
    
    public Product getSelection() throws CancelException {
        if (show() == true) {
            return productTableModel.get(productTable.convertRowIndexToModel(productTable.getSelectedRow()));
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
