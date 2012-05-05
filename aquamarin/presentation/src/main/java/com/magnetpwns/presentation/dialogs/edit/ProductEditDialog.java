/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import com.magnetpwns.presentation.model.GenericTableModel;
import com.magnetpwns.presentation.model.ProductTableModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class ProductEditDialog extends AbstractEditDialog<Product> {

    private JTextField codeField;
    private JTextField codeManufField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField priceBoughtField;
    private JComboBox manufCombo;
    private JComboBox taxRateCombo;
    private JComboBox unitCombo;
    
    private String description;

    public ProductEditDialog() {
        super("Product editor");
        description = "";
    }
    
    
    
    @Override
    protected Product parseObject() {
        BigDecimal priceBought;
        if (priceBoughtField.getText().length() == 0) {
            priceBought = BigDecimal.ZERO;
        } else {
            priceBought = new BigDecimal(priceBoughtField.getText());
        }
        
        return new Product(
                new ProductId(0),
                codeField.getText(),
                codeManufField.getText(),
                nameField.getText(),
                description,
                new BigDecimal(priceField.getText()),
                priceBought,
                (Manufacturer) manufCombo.getSelectedItem(),
                (TaxRate) taxRateCombo.getSelectedItem(),
                (Unit) unitCombo.getSelectedItem());
    }

    @Override
    protected void fillFromObject(Product o) {
        description = o.getDescription();
        
        codeField.setText(o.getCode());
        codeManufField.setText(o.getCodeManuf());
        nameField.setText(o.getName());
        priceField.setText(o.getPrice().toPlainString());
        priceBoughtField.setText(o.getPriceBought().toPlainString());
        
        // TODO manuf, taxrate, unit selection
    }

    @Override
    protected String[] getFormLabels() {
        return new String[] {
            "kód",
            "kód výrobce",
            "název",
            "cena",
            "nákupní cena",
            "výrobce",
            "DPH",
            "jednotka"
        };
    }

    @Override
    protected JComponent[] getFormComponents() {
        
        codeField = new JTextField();
        codeManufField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        priceBoughtField = new JTextField();
        
        manufCombo = new JComboBox((new DefaultComboBoxModel(
                AquamarinFacade.getDefault().findAllManufs().toArray())));
        taxRateCombo = new JComboBox((new DefaultComboBoxModel(
                AquamarinFacade.getDefault().findAllTaxRates().toArray())));
        unitCombo = new JComboBox((new DefaultComboBoxModel(
                AquamarinFacade.getDefault().findAllUnits().toArray())));
        
        return new JComponent[] {
            codeField,
            codeManufField,
            nameField,
            priceField,
            priceBoughtField,
            manufCombo,
            taxRateCombo,
            unitCombo
        };
    }

    @Override
    protected void resetFields() {
        codeField.setText("");
        codeManufField.setText("");
        nameField.setText("");
        priceField.setText("");
        priceBoughtField.setText("");
        
        
        codeField.requestFocusInWindow();
    }

    @Override
    protected GenericTableModel getTableModel() {
        return new ProductTableModel(AquamarinFacade.getDefault().findAllProducts());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<Product>() {

            @Override
            public Collection<Product> findAll() {
                return AquamarinFacade.getDefault().findAllProducts();
            }

            @Override
            public Product add(Product o) {
                return AquamarinFacade.getDefault().addProduct(o);
            }

            @Override
            public boolean delete(Product o) {
                return AquamarinFacade.getDefault().deleteProduct(o);
            }

            @Override
            public boolean update(Product o) {
                return AquamarinFacade.getDefault().updateProduct(o);
            }
        };
    }
}
