/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.edit;

import com.magnetpwns.model.Identifiable;
import com.magnetpwns.presentation.AddressBookTopComponent;
import com.magnetpwns.presentation.EditMode;
import com.magnetpwns.presentation.Fonts;
import com.magnetpwns.presentation.dialogs.AbstractDialog;
import com.magnetpwns.presentation.model.GenericTableModel;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public abstract class AbstractEditDialog<T extends Identifiable> extends AbstractDialog {

    private GenericTableModel<T> tableModel;
    private JTable table;
    
    private EditMode editMode = EditMode.ADD;
    private T editObject = null;
    
    FacadeMap<T> actionMap;

    public AbstractEditDialog(String title) {
        super(title);
        tableModel = getTableModel();
        actionMap = getFacadeMap();
    }
    
    protected abstract GenericTableModel getTableModel();
    protected abstract FacadeMap getFacadeMap();
    
    protected abstract T parseObject();
    protected abstract void fillFromObject(T o);
    protected abstract String[] getFormLabels();
    protected abstract JComponent[] getFormComponents();
    protected abstract void resetFields();
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        final JPanel formPanel = new JPanel();
        
        JButton addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/add24.png")));
        
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                editMode = EditMode.ADD;
                resetFields();
                ((TitledBorder) formPanel.getBorder()).setTitle(editMode.getLabel());
                formPanel.repaint();
            }
        });
        
        final JButton editButton = new JButton();
        editButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/edit24.png")));
        editButton.setEnabled(false);
        
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                editMode = EditMode.EDIT;
                editObject = tableModel.get(table.getSelectedRow());
                fillFromObject(editObject);
                ((TitledBorder) formPanel.getBorder()).setTitle(editMode.getLabel());
                formPanel.repaint();
            }
        });
        
        final JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (JOptionPane.showConfirmDialog(formPanel,
                        NbBundle.getMessage(AddressBookTopComponent.class, "CTL_DeleteConfirmation"),
                        NbBundle.getMessage(AddressBookTopComponent.class, "CTL_ConfirmAction"),
                        JOptionPane.YES_NO_OPTION)
                        == JOptionPane.YES_OPTION) {
                    if (actionMap.delete(tableModel.get(table.getSelectedRow()))) {
                        tableModel.remove(table.getSelectedRow());
                    }
                }
            }
        });
        
        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] labels = getFormLabels();
        
        formPanel.setLayout(new GridLayout(0, labels.length + 1, 5, 0));
        formPanel.setBorder(new TitledBorder(editMode.getLabel()));
        
        for (String string : labels) {
            formPanel.add(new JLabel(string));
        }
        
        formPanel.add(new JLabel(""));
        
        JComponent[] components = getFormComponents();
        
        for (JComponent jComponent : components) {
            formPanel.add(jComponent);
            jComponent.setFont(Fonts.BIG_INPUT);
        }
        
        JButton doneButton = new JButton();
        doneButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/ok24.png")));
        
        Action doneActionListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                T o;
                switch (editMode) {
                    case ADD:
                        o = parseObject();
                        if ((o = actionMap.add(o)) != null) {
                            tableModel.add(o);
                            resetFields();
                        }
                        break;
                    case EDIT:
                        o = parseObject();
                        o.setId(editObject.getId());
                        if (actionMap.update(o)) {
                            tableModel.update(o);
                            editMode = EditMode.ADD;
                            ((TitledBorder) formPanel.getBorder()).setTitle(editMode.getLabel());
                            formPanel.repaint();
                            resetFields();
                        }
                        break;
                }
            }
        };
        doneButton.addActionListener(doneActionListener);
        
        formPanel.add(doneButton);
        
        formPanel.getActionMap().put("done", doneActionListener);
        formPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "done");
        
        table = new JTable();
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                boolean enabled = !table.getSelectionModel().isSelectionEmpty();
                deleteButton.setEnabled(enabled);
                editButton.setEnabled(enabled);
            }
        });
        
        innerPane.add(toolbar);
        innerPane.add(formPanel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(scrollPane);
        
        //innerPane.revalidate();
        
        return innerPane;
    }

    @Override
    protected JButton[] getOptions() {
        return new JButton[] { getOkButton() };
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    result = true;
                }
            }
        };
    }
    
}
