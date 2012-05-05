/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.model.Client;
import com.magnetpwns.presentation.model.AddressTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author MagNet
 */
public class ClientListDialog extends AbstractDialog {

    private AddressTableModel clientsTableModel;
    private JTable clientsTable;
    
    private Collection<Client> clientList;
    
    public ClientListDialog(Collection<Client> clients) {
        super("Client list");
        clientsTableModel = new AddressTableModel(clients);
        clientList = clients;
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clientsTableModel.remove(clientsTable.getSelectedRow());
            }
        });
        
        JButton clearButton = new JButton();
        clearButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/clear24.png")));
        
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                clientsTableModel.clear();
            }
        });
        
        toolbar.add(deleteButton);
        toolbar.add(clearButton);
        
        clientsTable = new JTable();
        clientsTable.setModel(clientsTableModel);
        
        clientsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                deleteButton.setEnabled(!clientsTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        innerPane.add(toolbar);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }

    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    clientList.clear();
                    clientList.addAll(clientsTableModel.getItems());
                    result = true;
                }
            }
        };
    }
    
}
