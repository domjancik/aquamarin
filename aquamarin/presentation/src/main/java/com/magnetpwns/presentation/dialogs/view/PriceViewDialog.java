/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Price;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import com.magnetpwns.presentation.model.PriceTableModel;
import java.awt.Component;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author MagNet
 */
public class PriceViewDialog extends AbstractOkDialog {

    private PriceTableModel priceTableModel;
    private JTable productTable;
    
    Collection<Price> priceList;
    Product p;
    
    public PriceViewDialog(Product p) throws AquamarinException {
        super(p.toString() + " - Price list");
        AquamarinFacade.getDefault().loadProduct(p);
        priceTableModel = new PriceTableModel(p.getPriceList());
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        productTable = new JTable();
        productTable.setModel(priceTableModel);
        productTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }
}
