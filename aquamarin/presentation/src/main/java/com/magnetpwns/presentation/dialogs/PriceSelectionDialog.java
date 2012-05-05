/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Price;
import com.magnetpwns.model.PriceId;
import com.magnetpwns.model.Product;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.model.PriceTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author MagNet
 */
public class PriceSelectionDialog extends AbstractDialog {

    private PriceTableModel priceTableModel;
    private JTable productTable;
    
    Collection<Price> priceList;
    Product p;
    
    public PriceSelectionDialog(Product p) throws AquamarinException {
        super("Select price");
        this.p = p;
        AquamarinFacade.getDefault().loadProduct(p);
        priceList = p.getPriceList();
        if (priceList.size() == 1)
            throw new AquamarinException();
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
    
    public PriceId getSelection() throws CancelException {
        if (priceList.size() == 1) {
            return ((Price) priceList.toArray()[0]).getId();
        }
        if (show() == true) {
            return priceTableModel.get(productTable.convertRowIndexToModel(productTable.getSelectedRow())).getId();
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
                    p.setAltPriceId(priceTableModel.get(productTable.convertRowIndexToModel(productTable.getSelectedRow())).getId());
                    result = true;
                }
            }
        };
    }
    
}
