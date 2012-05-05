/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.table;

import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * JTable with column size adjusted to DocumentItemTableModel
 * @author MagNet
 */
public class DocumentItemTable extends AbstractSizedTable {

    public DocumentItemTable() {
    }

    public DocumentItemTable(TableModel dm) {
        super(dm);
    }

    public DocumentItemTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public DocumentItemTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public DocumentItemTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public DocumentItemTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
    }

    public DocumentItemTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }
    

    @Override
    protected int[] getSizes() {
        return new int[] {
            10,
            400,
            100,
            200,
            100,
            100,
            200
        };
    }
    
}
