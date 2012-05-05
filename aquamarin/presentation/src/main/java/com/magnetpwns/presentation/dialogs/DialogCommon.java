/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.presentation.model.StockItemTableModel;
import java.awt.GridLayout;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author MagNet
 */
public class DialogCommon {
    public static JPanel initProductFilterPanel(final TableRowSorter sorter,
            final Integer nameColumn) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(new TitledBorder("Filtr"));
        panel.add(new JLabel("kód"));
        panel.add(new JLabel("název"));
        
        final JTextField codeFilterField;
        final JTextField nameFilterField;
        panel.add(codeFilterField = new JTextField());
        panel.add(nameFilterField = new JTextField());
        
        codeFilterField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter(sorter, codeFilterField.getText(), 0);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter(sorter, codeFilterField.getText(), 0);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter(sorter, codeFilterField.getText(), 0);
            }
        });
        
        nameFilterField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter(sorter, nameFilterField.getText(), nameColumn);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter(sorter, nameFilterField.getText(), nameColumn);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter(sorter, nameFilterField.getText(), nameColumn);
            }
        });
        
        return panel;
    }
    
    private static void newFilter(TableRowSorter sorter, String filterText, Integer column)  {
        RowFilter<StockItemTableModel, Integer> rf = null;
        try {
            rf = RowFilter.regexFilter(filterText, column);
        } catch (PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
}
