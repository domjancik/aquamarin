/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.presentation.dialogs.edit.FacadeMap;
import com.magnetpwns.presentation.dialogs.edit.AbstractEditDialog;
import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.component.JTextFieldNumberDocument;
import com.magnetpwns.model.City;
import com.magnetpwns.model.CityId;
import com.magnetpwns.model.Country;
import com.magnetpwns.presentation.model.CityTableModel;
import com.magnetpwns.presentation.model.GenericTableModel;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class CityEditDialog extends AbstractEditDialog<City> {

    private JTextField nameField;
    private JTextField postcodeField;
    private JComboBox countryComboBox;

    public CityEditDialog() {
        super("City editor");
    }
    
    @Override
    protected City parseObject() {
        return new City(new CityId(0), Integer.parseInt(postcodeField.getText()), nameField.getText(),
                (Country) countryComboBox.getSelectedItem());
    }

    @Override
    protected void fillFromObject(City o) {
        nameField.setText(o.getName());
        postcodeField.setText(Integer.toString(o.getPostcode()));
    }

    @Override
    protected String[] getFormLabels() {
        return new String[] {
            "Název",
            "PSČ",
            "Země"
        };
    }

    @Override
    protected JComponent[] getFormComponents() {
        nameField = new JTextField();
        postcodeField = new JTextField();
        postcodeField.setDocument(new JTextFieldNumberDocument());
        countryComboBox = new JComboBox(AquamarinFacade.getDefault().findAllCountries().toArray());
        
        return new JComponent[] {
            nameField,
            postcodeField,
            countryComboBox
        };
    }

    @Override
    protected void resetFields() {
        nameField.setText("");
        postcodeField.setText("0");
        nameField.requestFocusInWindow();
    }

    @Override
    protected GenericTableModel getTableModel() {
        return new CityTableModel(AquamarinFacade.getDefault().findAllCities());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<City>() {

            @Override
            public Collection<City> findAll() {
                return AquamarinFacade.getDefault().findAllCities();
            }

            @Override
            public City add(City o) {
                return AquamarinFacade.getDefault().addCity(o);
            }

            @Override
            public boolean delete(City o) {
                return AquamarinFacade.getDefault().deleteCity(o);
            }

            @Override
            public boolean update(City o) {
                return AquamarinFacade.getDefault().updateCity(o);
            }
        };
    }
    
}
