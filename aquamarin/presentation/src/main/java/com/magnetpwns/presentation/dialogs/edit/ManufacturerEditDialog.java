/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Manufacturer;
import com.magnetpwns.model.ManufacturerId;
import com.magnetpwns.presentation.model.GenericTableModel;
import com.magnetpwns.presentation.model.ManufacturerTableModel;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class ManufacturerEditDialog extends AbstractEditDialog<Manufacturer> {

    private JTextField nameField;

    public ManufacturerEditDialog() {
        super("Manufacturer editor");
    }

    @Override
    protected Manufacturer parseObject() {
        return new Manufacturer(new ManufacturerId(0), nameField.getText());
    }

    @Override
    protected void fillFromObject(Manufacturer o) {
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
            nameField
        };
    }

    @Override
    protected void resetFields() {
        nameField.setText("");
        nameField.requestFocusInWindow();
    }

    @Override
    protected GenericTableModel getTableModel() {
        return new ManufacturerTableModel(AquamarinFacade.getDefault().findAllManufs());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<Manufacturer>() {

            @Override
            public Collection<Manufacturer> findAll() {
                return AquamarinFacade.getDefault().findAllManufs();
            }

            @Override
            public Manufacturer add(Manufacturer o) {
                return AquamarinFacade.getDefault().addManuf(o);
            }

            @Override
            public boolean delete(Manufacturer o) {
                return AquamarinFacade.getDefault().deleteManuf(o);
            }

            @Override
            public boolean update(Manufacturer o) {
                return AquamarinFacade.getDefault().updateManuf(o);
            }
        };
    }

}
