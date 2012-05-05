/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.model;

import com.magnetpwns.model.Product;
import java.util.Collection;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ProductTableModel extends GenericTableModel<Product> {

    public ProductTableModel(Collection<Product> data) {
        super(data);
    }
    
    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_Code");
            case 1:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_CodeManuf");
            case 2:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_Name");
            case 3:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_Description");
            case 4:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_Price");
            case 5:
                return NbBundle.getMessage(ProductTableModel.class, "TCN_PriceBought");
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Product p = items.get(i);
        
        switch (i1) {
            case 0:
                return p.getCode();
            case 1:
                return p.getCodeManuf();
            case 2:
                return p.toString();
            case 3:
                return p.getDescription();
            case 4:
                return p.getPrice();
            case 5:
                return p.getPriceBought();
            default:
                return "";
        }
    }
}
