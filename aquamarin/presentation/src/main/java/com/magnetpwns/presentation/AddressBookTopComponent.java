/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation;

import com.magnetpwns.presentation.dialogs.edit.CountryEditDialog;
import com.magnetpwns.presentation.dialogs.create.InvoiceCreateDialog;
import com.magnetpwns.presentation.dialogs.create.ProformaCreateDialog;
import com.magnetpwns.presentation.dialogs.create.DeliveryCreateDialog;
import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.component.JTextFieldLimitedDocument;
import com.magnetpwns.model.City;
import com.magnetpwns.model.Client;
import com.magnetpwns.model.ClientId;
import com.magnetpwns.presentation.component.JPopupList;
import com.magnetpwns.presentation.dialogs.edit.CityEditDialog;
import com.magnetpwns.presentation.dialogs.ClientListDialog;
import com.magnetpwns.component.JTextFieldNumberDocument;
import com.magnetpwns.model.*;
import com.magnetpwns.presentation.model.AddressTableModel;
import com.magnetpwns.printing.PostPrint;
import com.magnetpwns.printing.Print;
import com.magnetpwns.printing.StickerPrint;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.magnetpwns.presentation//AddressBook//EN",
autostore = false)
@TopComponent.Description(preferredID = "AddressBookTopComponent",
iconBase = "com/magnetpwns/presentation/addressbook.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.magnetpwns.presentation.AddressBookTopComponent")
@ActionReference(path = "Toolbars/Views" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_AddressBookAction",
preferredID = "AddressBookTopComponent")
public final class AddressBookTopComponent extends TopComponent {

    private List<Client> printClients;
    private EditMode editMode = EditMode.NONE;
    private AddressTableModel addressTableModel;
    
    private JPopupList popupList;
    
    private BigDecimal editSum;
    
    public AddressBookTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(AddressBookTopComponent.class, "CTL_AddressBookTopComponent"));
        setToolTipText(NbBundle.getMessage(AddressBookTopComponent.class, "HINT_AddressBookTopComponent"));
        
        printClients = new ArrayList<Client>();
        
        final ListSelectionModel sm = addressTable.getSelectionModel();
        sm.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                boolean enable = !sm.isSelectionEmpty();
                editButton.setEnabled(enable);
                deleteButton.setEnabled(enable);
                printAddButton.setEnabled(enable);
                createProformaButton.setEnabled(enable);
                createDeliveryButton.setEnabled(enable);
                createInvoiceButton.setEnabled(enable);
            }
        });
        
        TableColumn column;
        for (int i = 0; i < 6; i++) {
            column = addressTable.getColumnModel().getColumn(i);
            if (i == 0 || i == 5) {
                column.setPreferredWidth(10);
            } else {
                column.setPreferredWidth(200);
            }
        }
        
        // Keyboard shortcuts
        Action addAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        };
        
        Action editAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (editButton.isEnabled()) {
                    editButtonActionPerformed(e);
                }
            }
        };
        
        Action filterAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filterButtonActionPerformed(e);
            }
        };
        
        Action printAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printStickerButtonActionPerformed(e);
            }
        };
        
        Action okAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonActionPerformed(e);
            }
        };
        
        Action cancelAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        };
        
        Action proformaAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (createProformaButton.isEnabled())
                    createProformaButtonActionPerformed(e);
            }
        };
        
        Action deliveryAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (createDeliveryButton.isEnabled())
                    createDeliveryButtonActionPerformed(e);
            }
        };
        
        Action invoiceAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (createInvoiceButton.isEnabled())
                    createInvoiceButtonActionPerformed(e);
            }
        };
        
        Action printAddAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (printAddButton.isEnabled())
                    printAddButtonActionPerformed(e);
            }
        };
        
        getActionMap().put("add", addAction);
        getActionMap().put("edit", editAction);
        getActionMap().put("filter", filterAction);
        getActionMap().put("print", printAction);
        getActionMap().put("cancel", cancelAction);
        getActionMap().put("ok", okAction);
        getActionMap().put("proforma", proformaAction);
        getActionMap().put("delivery", deliveryAction);
        getActionMap().put("invoice", invoiceAction);
        getActionMap().put("printadd", printAddAction);
        InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("F5"), "add");
        im.put(KeyStroke.getKeyStroke("F6"), "edit");
        im.put(KeyStroke.getKeyStroke("F7"), "filter");
        im.put(KeyStroke.getKeyStroke("F10"), "print");
        im.put(KeyStroke.getKeyStroke("F2"), "proforma");
        im.put(KeyStroke.getKeyStroke("F3"), "delivery");
        im.put(KeyStroke.getKeyStroke("F4"), "invoice");
        im.put(KeyStroke.getKeyStroke("F9"), "printadd");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ok");
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        addressTable = new javax.swing.JTable();
        mainToolBar = new javax.swing.JToolBar();
        filterButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        documentToolBar = new javax.swing.JToolBar();
        createProformaButton = new javax.swing.JButton();
        createDeliveryButton = new javax.swing.JButton();
        createInvoiceButton = new javax.swing.JButton();
        printToolBar = new javax.swing.JToolBar();
        printAddButton = new javax.swing.JButton();
        printListButton = new javax.swing.JButton();
        printStickerButton = new javax.swing.JButton();
        printLetterButton = new javax.swing.JButton();
        cityToolBar = new javax.swing.JToolBar();
        cityButton = new javax.swing.JButton();
        countryButton = new javax.swing.JButton();
        postcodeImportButton = new javax.swing.JButton();
        formPanel = new javax.swing.JPanel();
        idField = new javax.swing.JTextField();
        titleField = new javax.swing.JTextField();
        titleShortField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        streetField = new javax.swing.JTextField();
        cityComboBox = new javax.swing.JComboBox();
        vipCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        icoField = new javax.swing.JTextField();
        dicField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        telWorkField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        telHomeField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        telCellField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        noteField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        stockCheckBox = new javax.swing.JCheckBox();

        addressTable.setAutoCreateRowSorter(true);
        addressTable.setModel(addressTableModel = new AddressTableModel(AquamarinFacade.getDefault().findAllClients()));
        jScrollPane1.setViewportView(addressTable);

        mainToolBar.setFloatable(false);
        mainToolBar.setRollover(true);

        filterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/filter24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(filterButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.filterButton.text")); // NOI18N
        filterButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.filterButton.toolTipText")); // NOI18N
        filterButton.setFocusable(false);
        filterButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filterButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });
        mainToolBar.add(filterButton);

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/add24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.addButton.text")); // NOI18N
        addButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.addButton.toolTipText")); // NOI18N
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        mainToolBar.add(addButton);

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/edit24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.editButton.text")); // NOI18N
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.editButton.toolTipText")); // NOI18N
        editButton.setEnabled(false);
        editButton.setFocusable(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        mainToolBar.add(editButton);

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.deleteButton.text")); // NOI18N
        deleteButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.deleteButton.toolTipText")); // NOI18N
        deleteButton.setEnabled(false);
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        mainToolBar.add(deleteButton);

        documentToolBar.setFloatable(false);
        documentToolBar.setRollover(true);

        createProformaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/proforma24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(createProformaButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createProformaButton.text")); // NOI18N
        createProformaButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createProformaButton.toolTipText")); // NOI18N
        createProformaButton.setEnabled(false);
        createProformaButton.setFocusable(false);
        createProformaButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createProformaButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createProformaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createProformaButtonActionPerformed(evt);
            }
        });
        documentToolBar.add(createProformaButton);

        createDeliveryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delivery24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(createDeliveryButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createDeliveryButton.text")); // NOI18N
        createDeliveryButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createDeliveryButton.toolTipText")); // NOI18N
        createDeliveryButton.setEnabled(false);
        createDeliveryButton.setFocusable(false);
        createDeliveryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createDeliveryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createDeliveryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDeliveryButtonActionPerformed(evt);
            }
        });
        documentToolBar.add(createDeliveryButton);

        createInvoiceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/invoice24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(createInvoiceButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createInvoiceButton.text")); // NOI18N
        createInvoiceButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.createInvoiceButton.toolTipText")); // NOI18N
        createInvoiceButton.setEnabled(false);
        createInvoiceButton.setFocusable(false);
        createInvoiceButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createInvoiceButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createInvoiceButtonActionPerformed(evt);
            }
        });
        documentToolBar.add(createInvoiceButton);

        printToolBar.setFloatable(false);
        printToolBar.setRollover(true);

        printAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/addsquare24.png"))); // NOI18N
        printAddButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printAddButton.toolTipText")); // NOI18N
        printAddButton.setEnabled(false);
        printAddButton.setFocusable(false);
        printAddButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printAddButton.setLabel(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printAddButton.label")); // NOI18N
        printAddButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printAddButtonActionPerformed(evt);
            }
        });
        printToolBar.add(printAddButton);

        printListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/list24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printListButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printListButton.text")); // NOI18N
        printListButton.setFocusable(false);
        printListButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printListButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printListButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printListButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printListButtonMouseExited(evt);
            }
        });
        printListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printListButtonActionPerformed(evt);
            }
        });
        printToolBar.add(printListButton);

        printStickerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/sticker24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printStickerButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printStickerButton.text")); // NOI18N
        printStickerButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printStickerButton.toolTipText")); // NOI18N
        printStickerButton.setFocusable(false);
        printStickerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printStickerButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printStickerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printStickerButtonActionPerformed(evt);
            }
        });
        printToolBar.add(printStickerButton);

        printLetterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/letter24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printLetterButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printLetterButton.text")); // NOI18N
        printLetterButton.setToolTipText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.printLetterButton.toolTipText")); // NOI18N
        printLetterButton.setFocusable(false);
        printLetterButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printLetterButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printLetterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printLetterButtonActionPerformed(evt);
            }
        });
        printToolBar.add(printLetterButton);

        cityToolBar.setFloatable(false);
        cityToolBar.setRollover(true);

        cityButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/cities24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(cityButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.cityButton.text")); // NOI18N
        cityButton.setFocusable(false);
        cityButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cityButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityButtonActionPerformed(evt);
            }
        });
        cityToolBar.add(cityButton);
        cityButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.cityButton.AccessibleContext.accessibleDescription")); // NOI18N

        countryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/country24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(countryButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.countryButton.text")); // NOI18N
        countryButton.setFocusable(false);
        countryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        countryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        countryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryButtonActionPerformed(evt);
            }
        });
        cityToolBar.add(countryButton);

        org.openide.awt.Mnemonics.setLocalizedText(postcodeImportButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.postcodeImportButton.text")); // NOI18N
        postcodeImportButton.setEnabled(false);
        postcodeImportButton.setFocusable(false);
        postcodeImportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        postcodeImportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        postcodeImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postcodeImportButtonActionPerformed(evt);
            }
        });
        cityToolBar.add(postcodeImportButton);

        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.formPanel.border.title"))); // NOI18N

        idField.setDocument(new JTextFieldNumberDocument());
        idField.setFont(Fonts.BIG_INPUT);
        idField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.idField.text")); // NOI18N

        titleField.setDocument(new JTextFieldLimitedDocument(150));
        titleField.setFont(Fonts.BIG_INPUT);
        titleField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.titleField.text")); // NOI18N

        titleShortField.setDocument(new JTextFieldLimitedDocument(50));
        titleShortField.setFont(Fonts.BIG_INPUT);
        titleShortField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.titleShortField.text")); // NOI18N

        nameField.setDocument(new JTextFieldLimitedDocument(50));
        nameField.setFont(Fonts.BIG_INPUT);
        nameField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.nameField.text")); // NOI18N

        streetField.setDocument(new JTextFieldLimitedDocument(50));
        streetField.setFont(Fonts.BIG_INPUT);
        streetField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.streetField.text")); // NOI18N

        cityComboBox.setFont(Fonts.BIG_INPUT);
        cityComboBox.setModel(new DefaultComboBoxModel(AquamarinFacade.getDefault().findAllCities().toArray()));

        vipCheckBox.setFont(Fonts.BIG_INPUT);
        org.openide.awt.Mnemonics.setLocalizedText(vipCheckBox, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.vipCheckBox.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel6.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel7.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel4.text")); // NOI18N

        icoField.setDocument(new JTextFieldNumberDocument());
        icoField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.icoField.text")); // NOI18N

        dicField.setDocument(new JTextFieldLimitedDocument(20));
        dicField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.dicField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel8.text")); // NOI18N

        telWorkField.setDocument(new JTextFieldLimitedDocument(20));
        telWorkField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.telWorkField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel9.text")); // NOI18N

        telHomeField.setDocument(new JTextFieldLimitedDocument(20));
        telHomeField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.telHomeField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel10.text")); // NOI18N

        telCellField.setDocument(new JTextFieldLimitedDocument(20));
        telCellField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.telCellField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel11.text")); // NOI18N

        emailField.setDocument(new JTextFieldLimitedDocument(50));
        emailField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.emailField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel12.text")); // NOI18N

        noteField.setDocument(new JTextFieldLimitedDocument(100));
        noteField.setText(org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.noteField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.jLabel13.text")); // NOI18N

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/ok24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/magnetpwns/presentation/clear24.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        stockCheckBox.setFont(Fonts.BIG_INPUT);
        org.openide.awt.Mnemonics.setLocalizedText(stockCheckBox, org.openide.util.NbBundle.getMessage(AddressBookTopComponent.class, "AddressBookTopComponent.stockCheckBox.text")); // NOI18N

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleShortField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(vipCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stockCheckBox)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(icoField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dicField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(telWorkField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(telHomeField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(telCellField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(noteField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPanelLayout.createSequentialGroup()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6))
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(titleField)
                                    .addComponent(titleShortField)
                                    .addComponent(nameField)
                                    .addComponent(streetField)
                                    .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(vipCheckBox)
                                    .addComponent(stockCheckBox)
                                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29))
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))))
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(icoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dicField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(telWorkField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(telHomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(telCellField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, formPanelLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(formPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(mainToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(documentToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(printToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cityToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(documentToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        formPanel.setVisible(false);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        prepareForm(EditMode.ADD);
    }//GEN-LAST:event_addButtonActionPerformed

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterButtonActionPerformed
        prepareForm(EditMode.FILTER);
    }//GEN-LAST:event_filterButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        prepareForm(EditMode.EDIT);
    }//GEN-LAST:event_editButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        int ico;
        int id;
        
        try {
            ico = Integer.parseInt(icoField.getText());
        } catch (NumberFormatException e) {
            ico = 0;
        }
        
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            id = 0;
        }
        
        Client c = new Client(
                new ClientId(id),
                titleField.getText(),
                titleShortField.getText(),
                nameField.getText(),
                vipCheckBox.isSelected(),
                streetField.getText(),
                ico,
                dicField.getText(),
                telWorkField.getText(),
                telHomeField.getText(),
                telCellField.getText(),
                emailField.getText(),
                noteField.getText(),
                (City)cityComboBox.getSelectedItem(),
                editSum,
                stockCheckBox.isSelected());
        switch (editMode) {
            case ADD:
                if ((c = AquamarinFacade.getDefault().addClient(c)) != null) {
                    addressTableModel.add(c);
                }
                break;
            case EDIT:
                if (AquamarinFacade.getDefault().updateClient(c)) {
                    addressTableModel.update(c);
                }
                break;
            case FILTER:
                addressTableModel.setItems(
                        AquamarinFacade.getDefault().findFilteredClients(c));
                break;
                
        }
        formPanel.setVisible(false);
        editMode = EditMode.NONE;
    }//GEN-LAST:event_okButtonActionPerformed

    private void printListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printListButtonActionPerformed
        ArrayList<Client> printClientsArray = new ArrayList<Client>(printClients);
        if (new ClientListDialog(printClientsArray).show()) {
            for (Client client : printClientsArray) {
                System.out.println(client.toString());
            }
            printClients = printClientsArray;
        }
        
        
    }//GEN-LAST:event_printListButtonActionPerformed

    private void printAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printAddButtonActionPerformed
        for (int i : addressTable.getSelectedRows()) {
            printClients.add(((AddressTableModel) addressTable.getModel()).get(getRealRow(i)));
        }
    }//GEN-LAST:event_printAddButtonActionPerformed

    private void cityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityButtonActionPerformed
        new CityEditDialog().show();
        cityComboBox.setModel(new DefaultComboBoxModel(AquamarinFacade.getDefault().findAllCities().toArray()));
    }//GEN-LAST:event_cityButtonActionPerformed

    private int getRealRow(int index) {
        return addressTable.convertRowIndexToModel(index);
    }
    
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int result = JOptionPane.showConfirmDialog(this, NbBundle.getMessage(AddressBookTopComponent.class, "CTL_DeleteConfirmation"), NbBundle.getMessage(AddressBookTopComponent.class, "CTL_ConfirmAction"), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            int realRow = getRealRow(addressTable.getSelectedRow());
            if (AquamarinFacade.getDefault().deleteClient(addressTableModel.get(realRow))) {
                addressTableModel.remove(realRow); // TODO: Multiple deletion
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void postcodeImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postcodeImportButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        int retVal = chooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                String strLine;
                Pattern p = Pattern.compile(";");
                Country country = AquamarinFacade.getDefault().findCountry(new CountryId(1));
                while ((strLine = br.readLine()) != null) {
                    String[] elems = p.split(strLine);
                    AquamarinFacade.getDefault().addCity(
                            new City(null, Integer.parseInt(elems[0]), elems[1], country));
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }//GEN-LAST:event_postcodeImportButtonActionPerformed

    /**
     * Checks if there are selected clients for print, otherwise shows a message
     * @return true if there are clients, false otherwise
     */
    private boolean checkPrint() {
        if (printClients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nejsou vybráni žádní odběratelé");
            return false;
        }
        return true;
    }
    
    private void printStickerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printStickerButtonActionPerformed
        if (checkPrint())
            Print.print(new StickerPrint(printClients));
    }//GEN-LAST:event_printStickerButtonActionPerformed

    private void printListButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printListButtonMouseEntered
        int x = printListButton.getX() + printListButton.getWidth() / 2 + printToolBar.getX();
        int y = printListButton.getY() + printListButton.getHeight() + printToolBar.getY();
        
        popupList = new JPopupList(x, y, printClients);
        popupList.paint(getGraphics());
    }//GEN-LAST:event_printListButtonMouseEntered

    private void printListButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printListButtonMouseExited
        if (popupList != null) {
            popupList.clear(this);
            popupList = null;
        }
    }//GEN-LAST:event_printListButtonMouseExited

    private void printLetterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printLetterButtonActionPerformed
        if (checkPrint())
            Print.print(new PostPrint(printClients));
    }//GEN-LAST:event_printLetterButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        formPanel.setVisible(false);
        editMode = EditMode.NONE;
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void countryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryButtonActionPerformed
        new CountryEditDialog().show();
    }//GEN-LAST:event_countryButtonActionPerformed

    private void createProformaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createProformaButtonActionPerformed
        new ProformaCreateDialog(getSelectedClient()).show();
    }//GEN-LAST:event_createProformaButtonActionPerformed

    private void createDeliveryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDeliveryButtonActionPerformed
        new DeliveryCreateDialog(getSelectedClient()).show();
    }//GEN-LAST:event_createDeliveryButtonActionPerformed

    private void createInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createInvoiceButtonActionPerformed
        new InvoiceCreateDialog(getSelectedClient()).show();
    }//GEN-LAST:event_createInvoiceButtonActionPerformed

    private void prepareForm(EditMode mode) {
        editMode = mode;
        ((TitledBorder) formPanel.getBorder()).setTitle(editMode.getLabel());
        formPanel.setVisible(true);
        formPanel.repaint();
        titleField.requestFocusInWindow();
        
        editSum = BigDecimal.ZERO;
        
        switch (editMode) {
            case ADD:
                resetForm();
                idField.setText("0");
                break;
            case EDIT:
                Client c = getSelectedClient();
                idField.setText(Integer.toString(c.getId().getId()));
                idField.setEnabled(false);
                titleField.setText(c.getTitle());
                titleShortField.setText(c.getTitleShort());
                nameField.setText(c.getName());
                streetField.setText(c.getStreet());
                cityComboBox.setSelectedItem(c.getCity());
                vipCheckBox.setSelected(c.isVip());
                stockCheckBox.setSelected(c.isStockEnabled());
                icoField.setText(Integer.toString(c.getIco()));
                dicField.setText(c.getDic());
                telWorkField.setText(c.getTelephoneWork());
                telHomeField.setText(c.getTelephoneHome());
                telCellField.setText(c.getTelephoneCell());
                emailField.setText(c.getEmail());
                noteField.setText(c.getNote());
                editSum = c.getTempSum();
                break;
            case FILTER:
                resetForm();
                break;
        }
    }

    private Client getSelectedClient() {
        return addressTableModel.get(addressTable.convertRowIndexToModel(addressTable.getSelectedRow()));
    }
    
    private void resetForm() {
        idField.setText("");
        idField.setEnabled(true);
        titleField.setText("");
        titleShortField.setText("");
        nameField.setText("");
        streetField.setText("");
        vipCheckBox.setSelected(false);
        stockCheckBox.setSelected(false);
        icoField.setText("");
        dicField.setText("");
        telWorkField.setText("");
        telHomeField.setText("");
        telCellField.setText("");
        noteField.setText("");
        emailField.setText("");
    }
    
    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        if (popupList != null) {
            popupList.paint(grphcs);
        }
    }

    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable addressTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cityButton;
    private javax.swing.JComboBox cityComboBox;
    private javax.swing.JToolBar cityToolBar;
    private javax.swing.JButton countryButton;
    private javax.swing.JButton createDeliveryButton;
    private javax.swing.JButton createInvoiceButton;
    private javax.swing.JButton createProformaButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField dicField;
    private javax.swing.JToolBar documentToolBar;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton filterButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField icoField;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar mainToolBar;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField noteField;
    private javax.swing.JButton okButton;
    private javax.swing.JButton postcodeImportButton;
    private javax.swing.JButton printAddButton;
    private javax.swing.JButton printLetterButton;
    private javax.swing.JButton printListButton;
    private javax.swing.JButton printStickerButton;
    private javax.swing.JToolBar printToolBar;
    private javax.swing.JCheckBox stockCheckBox;
    private javax.swing.JTextField streetField;
    private javax.swing.JTextField telCellField;
    private javax.swing.JTextField telHomeField;
    private javax.swing.JTextField telWorkField;
    private javax.swing.JTextField titleField;
    private javax.swing.JTextField titleShortField;
    private javax.swing.JCheckBox vipCheckBox;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component openinga
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
