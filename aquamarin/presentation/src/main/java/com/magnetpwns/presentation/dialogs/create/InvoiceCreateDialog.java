/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.create;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.StockTopComponent;
import com.magnetpwns.presentation.dialogs.DialogCommon;
import com.magnetpwns.presentation.dialogs.PriceSelectionDialog;
import com.magnetpwns.presentation.dialogs.RequestDialog;
import com.magnetpwns.presentation.dialogs.StockDialog;
import com.magnetpwns.presentation.dialogs.view.InvoicePreviewDialog;
import com.magnetpwns.presentation.model.DocumentItemTableModel;
import com.magnetpwns.presentation.model.StockItemTableModel;
import com.magnetpwns.presentation.table.DocumentItemTable;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class InvoiceCreateDialog extends AbstractCreateDialog<Invoice> {

    private DocumentItemTableModel documentItemTableModel;
    private JTable documentItemTable;
    private StockItemTableModel stockItemTableModel;
    private JTable stockItemTable;
    
    private Client client;
    private Stock sourceStock;
    private Stock favoriteStock = null;
    
    private JPanel addSpecPanel;
    
    
    private JTextField codeFilterField;
    private JTextField nameFilterField;
    
    private JTextField itemCodeField;
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private JComboBox<TaxRate> itemTaxRateCombo;
    private JComboBox<Unit> itemUnitCombo;
    private JSpinner itemCountSpinner;
    
    private JButton stockSelectButton;
    private JButton favoriteStockButton;
    private JButton setDiscountButton;
    
    private JButton deleteButton;
    private JButton setDiscountPriceButton;
    private JButton addSpecialButton;
    private JButton addSpecialPostButton;
    private JButton addSpecialServiceButton;
    
    public InvoiceCreateDialog(Client klient) {
        super(NbBundle.getMessage(InvoiceCreateDialog.class, "CTL_DialogCreateInvoice") + klient.toString());
        this.client = klient;
        documentItemTableModel = new DocumentItemTableModel(new ArrayList<DocumentItem>());
        document = new Invoice(
                null, client, null, documentItemTableModel.getItems(),
                null, null, null, PaymentType.TRANSFER, null, BigDecimal.ZERO, null, 0);
        document.setItems(documentItemTableModel.getItems());
        stockItemTableModel = new StockItemTableModel(new ArrayList<StockItem>());
        
        int favoriteStockOwnerId = NbPreferences.forModule(StockTopComponent.class).getInt("favoriteStockOwner", 0);
        if (favoriteStockOwnerId != 0) {
            Client favoriteStockOwner = AquamarinFacade.getDefault().findClient(new ClientId(favoriteStockOwnerId));
            favoriteStock = AquamarinFacade.getDefault().findStock(favoriteStockOwner);
        }
        
        getOkButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Invoice i = new Invoice(
                            null,
                            client,
                            issueDatePicker.getDate(),
                            documentItemTableModel.getItems(),
                            noteField.getText(),
                            deliveryNoteField.getText(),
                            validDatePicker.getDate(),
                            (PaymentType) paymentTypeComboBox.getSelectedItem(),
                            null, null, null, 0);
                if (new InvoicePreviewDialog(i).show()) {
                    AquamarinFacade.getDefault().addInvoice(i);
                    dispose();
                }
            }
        });
    }
    
    private void setSourceStock(Stock stock) {
        sourceStock = stock;
        stockItemTableModel.setItems(stock.getItems());
    }
    
    private void selectFavoriteStock() {
        sourceStock = favoriteStock;
        stockItemTableModel.setItems(sourceStock.getItems());
    }
    
    private void clearSpecItemPanel() {
        itemCodeField.setEnabled(true);
        itemNameField.setEnabled(true);
        itemTaxRateCombo.setEnabled(true);
        clear(itemCodeField);
        clear(itemNameField);
        clear(itemPriceField);
        itemCountSpinner.setValue(1);
        addSpecPanel.setVisible(true);
        itemCodeField.requestFocusInWindow();
    }
    
    private void prepareSpecItemPanel(String code, String name) {
        clearSpecItemPanel();
        itemCodeField.setText(code);
        itemNameField.setText(name);
        itemCodeField.setEnabled(false);
        itemNameField.setEnabled(false);
        itemPriceField.requestFocusInWindow();
    }
    
    private void tryTaxSelection(BigDecimal taxRate) {
        for (int i = 0; i < itemTaxRateCombo.getItemCount(); i++) {
            if (itemTaxRateCombo.getItemAt(i).getTaxRate().compareTo(taxRate) == 0) {
                itemTaxRateCombo.setSelectedIndex(i);
                itemTaxRateCombo.setEnabled(false);
                return;
            }
        }
    }
    
    private JToolBar initToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton stockEditButton = new JButton();
        stockEditButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/adddelete24.png")));
        
        stockEditButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new StockDialog(sourceStock).show();
                reloadStock();
            }
        });
        
        stockSelectButton = new JButton();
        stockSelectButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/stockselect24.png")));
        
        stockSelectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Client sourceStockOwner = client;
                setSourceStock(AquamarinFacade.getDefault().findStock(sourceStockOwner));
            }
        });
        
        favoriteStockButton = new JButton();
        favoriteStockButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/favorite24.png")));
        
        favoriteStockButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectFavoriteStock();
            }
        });
        
        deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/delete24.png")));
        deleteButton.setEnabled(false);
        
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TrackedProduct selectedItem = getSelectedItem();
                    int amount = RequestDialog.showIntegerInputDialog();
                    while (amount > selectedItem.getAmount()) {
                        JOptionPane.showMessageDialog(null, "Na tomto dokumentu není dostatek zboží");
                        amount = RequestDialog.showIntegerInputDialog();
                    }
                    selectedItem.alterAmount(-amount);
                    documentItemTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        setDiscountButton = new JButton();
        setDiscountButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/discount24.png")));
        setDiscountButton.setEnabled(false);
        
        setDiscountButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BigDecimal discount = RequestDialog.showBigDecimalInputDialog();
                    DocumentItem selectedItem = getSelectedItem();
                    selectedItem.setDiscount(discount);
                    documentItemTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        setDiscountPriceButton = new JButton();
        setDiscountPriceButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/discountprice24.png")));
        setDiscountPriceButton.setEnabled(false);
        
        setDiscountPriceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BigDecimal price = RequestDialog.showBigDecimalInputDialog();
                    DocumentItem selectedItem = getSelectedItem();
                    BigDecimal ratio =
                            price.divide(
                            selectedItem.getProduct().getPrice(), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimalConstant.BD100);
                    BigDecimal discount = BigDecimalConstant.BD100.subtract(ratio);
                    selectedItem.setDiscount(discount);
                    documentItemTableModel.fireTableDataChanged();
                    updateTotal();
                } catch (CancelException ex) {
                    // Disregard operation
                }
            }
        });
        
        addSpecialButton = new JButton();
        addSpecialButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/add24.png")));
        addSpecialButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clearSpecItemPanel();
            }
        });
        
        addSpecialPostButton = new JButton();
        addSpecialPostButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/addpost24.png")));
        addSpecialPostButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                prepareSpecItemPanel(
                        NbBundle.getMessage(getClass(), "ITEM_PostCode"),
                        NbBundle.getMessage(getClass(), "ITEM_Post"));
                tryTaxSelection(new BigDecimal(20));
            }
        });
        
        addSpecialServiceButton = new JButton();
        addSpecialServiceButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/service24.png")));
        addSpecialServiceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                prepareSpecItemPanel(
                        NbBundle.getMessage(getClass(), "ITEM_ServiceCode"),
                        NbBundle.getMessage(getClass(), "ITEM_Service"));
                tryTaxSelection(new BigDecimal(14));
            }
        });
        
        toolbar.add(stockEditButton);
        if (favoriteStock != null)
            toolbar.add(favoriteStockButton);
        if (client.isStockEnabled())
            toolbar.add(stockSelectButton);
        toolbar.add(deleteButton);
        toolbar.add(setDiscountButton);
        toolbar.add(setDiscountPriceButton);
        toolbar.add(addSpecialButton);
        toolbar.add(addSpecialPostButton);
        toolbar.add(addSpecialServiceButton);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        return toolbar;
    }
    
    private JPanel initAddSpecPanel() {
        addSpecPanel = new JPanel();
        addSpecPanel.setLayout(new GridLayout(2, 8));
        addSpecPanel.setBorder(new TitledBorder("Přidat speciální položku"));
        addSpecPanel.add(new JLabel("kód"));
        addSpecPanel.add(new JLabel("název"));
        addSpecPanel.add(new JLabel("cena"));
        addSpecPanel.add(new JLabel("DPH"));
        addSpecPanel.add(new JLabel("jednotka"));
        addSpecPanel.add(new JLabel("počet"));
        addSpecPanel.add(new JLabel(""));
        addSpecPanel.add(new JLabel(""));
        addSpecPanel.add(itemCodeField = new JTextField());
        addSpecPanel.add(itemNameField = new JTextField());
        addSpecPanel.add(itemPriceField = new JTextField());
        addSpecPanel.add(itemTaxRateCombo = new JComboBox(AquamarinFacade.getDefault().findAllTaxRates().toArray()));
        addSpecPanel.add(itemUnitCombo = new JComboBox(AquamarinFacade.getDefault().findAllUnits().toArray()));
        addSpecPanel.add(itemCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1)));
        
        JButton itemAddButton = new JButton(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/ok24.png")));
        addSpecPanel.add(itemAddButton);
        JButton cancelButton = new JButton(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/clear24.png")));
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addSpecPanel.setVisible(false);
            }
        });
        addSpecPanel.add(cancelButton);
        
        Action itemAddActionListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String code = itemCodeField.getText();
                String name = itemNameField.getText();
                BigDecimal price = new BigDecimal(itemPriceField.getText());
                TaxRate taxRate = (TaxRate) itemTaxRateCombo.getSelectedItem();
                Unit unit = (Unit) itemUnitCombo.getSelectedItem();
                Integer count = (Integer) itemCountSpinner.getValue();
                DocumentItem di = new DocumentItem(
                        new Product(ProductId.SPECIFIC, code, null, name, null, price,
                        BigDecimal.ZERO, Manufacturer.DUMMY, taxRate, unit), count, BigDecimal.ZERO, Client.DUMMY);
                documentItemTableModel.add(di);
                
                addSpecPanel.setVisible(false);

                updateTotal();
            }
        };
        
        itemAddButton.addActionListener(itemAddActionListener);
        
        addSpecPanel.getActionMap().put("done", itemAddActionListener);
        addSpecPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "done");

        addSpecPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addSpecPanel.setVisible(false);
        return addSpecPanel;
    }
    
    private void reloadStock() {
        sourceStock = AquamarinFacade.getDefault().findStock(sourceStock.getOwner());
        stockItemTableModel.setItems(sourceStock.getItems());
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        documentItemTable = new DocumentItemTable(documentItemTableModel);
        stockItemTable = new JTable(stockItemTableModel);
        if (favoriteStock != null) {
            selectFavoriteStock();
        }
        TableRowSorter<StockItemTableModel> stockSorter = new TableRowSorter<StockItemTableModel>(stockItemTableModel);
        stockItemTable.setRowSorter(stockSorter);
        
        innerPane.add(initToolbar());
        innerPane.add(initAddSpecPanel());  
        
        documentItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean enable = !documentItemTable.getSelectionModel().isSelectionEmpty();
                deleteButton.setEnabled(enable);
                setDiscountButton.setEnabled(enable);
                setDiscountPriceButton.setEnabled(enable);
            }
        });
        
        innerPane.add(initPaymentTypeInput());
        innerPane.add(initNoteInput());
        innerPane.add(initTwoDateInput());
        
        JPanel tablePane = new JPanel();
        tablePane.setLayout(new BoxLayout(tablePane, BoxLayout.LINE_AXIS));
        tablePane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(documentItemTable);
        leftPane.add(scrollPane);
        tablePane.add(leftPane);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOrientation(JToolBar.VERTICAL);
        
        final JButton transferLeftButton = new JButton();
        transferLeftButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/transferleft24.png")));
        transferLeftButton.setEnabled(false);
        
        transferLeftButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StockItem selected = getSelectedStockItem();
                    TrackedProduct tp = findItem(selected);
                    int amount;
                    do {
                        amount = RequestDialog.showIntegerInputDialog();
                        if (tp != null) {
                            amount += tp.getAmount();
                        }
                    } while (amount > selected.getAvailableAmount());
                    try {
                        new PriceSelectionDialog(selected.getProduct()).show();
                    } catch (AquamarinException ex) {}
                    documentItemTableModel.alter(
                            new TrackedProduct(selected.getProduct(), amount, sourceStock.getOwner()));
                    updateTotal();
                } catch (CancelException ex) {
                }
            }
        });
                
        stockItemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                transferLeftButton.setEnabled(!stockItemTable.getSelectionModel().isSelectionEmpty());
            }
        });
        
        toolbar.add(transferLeftButton);
        tablePane.add(toolbar);
        
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(stockItemTable);
        
        rightPane.add(DialogCommon.initProductFilterPanel(stockSorter, 1));
        rightPane.add(scrollPane);
        
        tablePane.add(rightPane);
        
        innerPane.add(tablePane);
        
        innerPane.add(initTotalLabel());
        
        return innerPane;
    }

    private DocumentItem findItem(StockItem si) {
        Collection<DocumentItem> items = documentItemTableModel.getItems();
        for (DocumentItem trackedProduct : items) {
            if (trackedProduct.getSourceStock().equals(sourceStock.getOwner()) && trackedProduct.getProduct().equals(si.getProduct()))
                return trackedProduct;
        }
        return null;
    }
    
    private DocumentItem getSelectedItem() {
        return documentItemTableModel.get(documentItemTable.convertRowIndexToModel(documentItemTable.getSelectedRow()));
    }
    
    private StockItem getSelectedStockItem() {
        return stockItemTableModel.get(stockItemTable.convertRowIndexToModel(stockItemTable.getSelectedRow()));
    }
   

    @Override
    protected JButton[] getClosingOption() {
        return new JButton[] {
            getCancelButton()
        };
    }
}
