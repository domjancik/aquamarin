/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Identifiable;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MagNet
 */
public abstract class GenericTableModel<T extends Identifiable> extends AbstractTableModel {

    protected ArrayList<T> items;
    
    public GenericTableModel(Collection<T> data) {
        //items = (ArrayList<T>) data;
        items = new ArrayList<T>(data);
    }
    
    
    @Override
    public int getRowCount() {
        return items.size();
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    /**
     * Fill the table model with given data.
     * @param t 
     */
    public void fill(ArrayList<T> t) {
        items = t;
    }
    
    public T get(int i) {
        return items.get(i);
    }
    
    public void remove(int i) {
        items.remove(i);
        fireTableDataChanged();
    }
    
    public void clear() {
        items.clear();
        fireTableDataChanged();
    }
    
    public void add(T t) {
        items.add(t);
        fireTableDataChanged();
    }
    
    public void update(T t) {
        int i = 0;
        for (T item : items) {
            if (item.getId().equals(t.getId())) {
                items.set(i, t);
                break;
            }
            i++;
        }
        fireTableDataChanged();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = (ArrayList<T>) items;
        fireTableDataChanged();
    }
}
