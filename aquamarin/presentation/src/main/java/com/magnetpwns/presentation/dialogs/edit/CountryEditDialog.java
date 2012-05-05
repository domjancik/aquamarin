/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.component.JTextFieldNumberDocument;
import com.magnetpwns.model.Country;
import com.magnetpwns.model.CountryId;
import com.magnetpwns.presentation.model.GenericTableModel;
import com.magnetpwns.presentation.model.CountryTableModel;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class CountryEditDialog extends AbstractEditDialog<Country> {

    private JTextField nameField;

    public CountryEditDialog() {
        super("Country editor");
    }
    
    @Override
    protected Country parseObject() {
        return new Country(new CountryId(0), nameField.getText());
    }

    @Override
    protected void fillFromObject(Country o) {
        nameField.setText(o.getName());
    }

    @Override
    protected String[] getFormLabels() {
        return new String[] {
            "Název",
        };
    }

    @Override
    protected JComponent[] getFormComponents() {
        nameField = new JTextField();
        
        return new JComponent[] {
            nameField,
        };
    }

    @Override
    protected void resetFields() {
        nameField.setText("");
        nameField.requestFocusInWindow();
    }

    @Override
    protected GenericTableModel getTableModel() {
        return new CountryTableModel(AquamarinFacade.getDefault().findAllCountries());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<Country>() {

            @Override
            public Collection<Country> findAll() {
                return AquamarinFacade.getDefault().findAllCountries();
            }

            @Override
            public Country add(Country o) {
                return AquamarinFacade.getDefault().addCountry(o);
            }

            @Override
            public boolean delete(Country o) {
                return AquamarinFacade.getDefault().deleteCountry(o);
            }

            @Override
            public boolean update(Country o) {
                return AquamarinFacade.getDefault().updateCountry(o);
            }
        };
    }

}
