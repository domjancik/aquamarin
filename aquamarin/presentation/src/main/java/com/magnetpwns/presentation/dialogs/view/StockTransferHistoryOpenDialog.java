/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.model.StockTransfer;
import com.magnetpwns.presentation.dialogs.AbstractOkDialog;
import com.magnetpwns.presentation.model.CountedProductTableModel;
import com.magnetpwns.printing.Print;
import com.magnetpwns.printing.StockTransferPrint;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class StockTransferHistoryOpenDialog extends AbstractOkDialog {

    private CountedProductTableModel countedProductTableModel;
    private JTable countedProductTable;

    private StockTransfer stockTransfer;
    
    private static String getTitle(StockTransfer stockTransfer) {
        String title = NbBundle.getMessage(StockTransferHistoryOpenDialog.class, "CTL_StockTransferDetail") + " ";
        if (stockTransfer.getSourceStock() != null) {
            title += stockTransfer.getSourceStock().toString() + "->";
        }
        return title + stockTransfer.getDestinationStock().toString();
    }
    
    public StockTransferHistoryOpenDialog(StockTransfer stockTransfer) {
        super(getTitle(stockTransfer));
        this.stockTransfer = stockTransfer;
        countedProductTableModel = new CountedProductTableModel(stockTransfer.getItems());
    }
    
    private JToolBar initToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        JButton printButton = new JButton(
                new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/print24.png")));
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Print.print(new StockTransferPrint(stockTransfer));
            }
        });
        
        toolbar.add(printButton);
        toolbar.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        
        return toolbar;
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        if (stockTransfer.getSourceStock() != null) {
            innerPane.add(initToolbar());
        }
        
        countedProductTable = new JTable();
        countedProductTable.setModel(countedProductTableModel);

        JScrollPane scrollPane = new JScrollPane(countedProductTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        return innerPane;
    }    
}
