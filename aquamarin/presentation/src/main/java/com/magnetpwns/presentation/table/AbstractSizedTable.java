/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.table;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author MagNet
 */
public abstract class AbstractSizedTable extends JTable {

    private void initSizes() {
        int[] sizes = getSizes();
        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn tc = getColumnModel().getColumn(i);
            tc.setPreferredWidth(sizes[i]);
        }
    }

    public AbstractSizedTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        initSizes();
    }

    public AbstractSizedTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        initSizes();
    }

    public AbstractSizedTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        initSizes();
    }

    public AbstractSizedTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        initSizes();
    }

    public AbstractSizedTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        initSizes();
    }

    public AbstractSizedTable(TableModel dm) {
        super(dm);
        initSizes();
    }

    public AbstractSizedTable() {
        super();
        initSizes();
    }
    
    
    
    protected abstract int[] getSizes();
    
}
