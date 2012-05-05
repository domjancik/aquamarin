/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Unit;
import com.magnetpwns.model.UnitId;
import com.magnetpwns.presentation.model.GenericTableModel;
import com.magnetpwns.presentation.model.UnitTableModel;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author MagNet
 */
public class UnitEditDialog extends AbstractEditDialog<Unit> {

    private JTextField nameField;

    public UnitEditDialog() {
        super("Unit editor");
    }
    
    
    
    @Override
    protected Unit parseObject() {
        return new Unit(new UnitId(0), nameField.getText());
    }

    @Override
    protected void fillFromObject(Unit o) {
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
        return new UnitTableModel(AquamarinFacade.getDefault().findAllUnits());
    }

    @Override
    protected FacadeMap getFacadeMap() {
        return new FacadeMap<Unit>() {

            @Override
            public Collection<Unit> findAll() {
                return AquamarinFacade.getDefault().findAllUnits();
            }

            @Override
            public Unit add(Unit o) {
                return AquamarinFacade.getDefault().addUnit(o);
            }

            @Override
            public boolean delete(Unit o) {
                return AquamarinFacade.getDefault().deleteUnit(o);
            }

            @Override
            public boolean update(Unit o) {
                return AquamarinFacade.getDefault().updateUnit(o);
            }
        };
    }
}
