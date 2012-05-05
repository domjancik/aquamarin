/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.TaxRate;
import com.magnetpwns.model.TaxRateId;
import com.magnetpwns.presentation.model.GenericTableModel;
import com.magnetpwns.presentation.model.TaxRateTableModel;
import java.math.BigDecimal;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class TaxRateEditDialog extends AbstractEditDialog<TaxRate> {

    private JTextField nameField;
    private JTextField taxRateField;

    public TaxRateEditDialog() {
        super("Tax rate editor");
    }
    
    
    
    @Override
    protected TaxRate parseObject() {
        return new TaxRate(new TaxRateId(0), nameField.getText(), new BigDecimal(taxRateField.getText()));
    }

    @Override
    protected void fillFromObject(TaxRate o) {
        nameField.setText(o.getName());
        taxRateField.setText(o.getTaxRate().toPlainString());
    }

    @Override
    protected String[] getFormLabels() {
        return new String[] {
            "Název",
            "Sazba"
        };
    }

    @Override
    protected JComponent[] getFormComponents() {
        nameField = new JTextField();
        taxRateField = new JTextField();
        
        return new JComponent[] {
            nameField,
            taxRateField
        };
    }

    @Override
    protected void resetFields() {
        nameField.setText("");
        taxRateField.setText("");
        nameField.requestFocusInWindow();
    }

    @Override
    protected GenericTableModel getTableModel() {
        return new TaxRateTableModel(AquamarinFacade.getDefault().findAllTaxRates());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<TaxRate>() {

            @Override
            public Collection<TaxRate> findAll() {
                return AquamarinFacade.getDefault().findAllTaxRates();
            }

            @Override
            public TaxRate add(TaxRate o) {
                return AquamarinFacade.getDefault().addTaxRate(o);
            }

            @Override
            public boolean delete(TaxRate o) {
                return AquamarinFacade.getDefault().deleteTaxRate(o);
            }

            @Override
            public boolean update(TaxRate o) {
                return AquamarinFacade.getDefault().updateTaxRate(o);
            }
        };
    }
}
