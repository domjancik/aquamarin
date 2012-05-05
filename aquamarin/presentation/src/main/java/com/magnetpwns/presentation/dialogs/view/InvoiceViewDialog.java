/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.format.Format;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.Payment;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.presentation.CancelException;
import com.magnetpwns.presentation.dialogs.RequestDialog;
import com.magnetpwns.presentation.model.DocumentItemTableModel;
import com.magnetpwns.presentation.model.PaymentTableModel;
import com.magnetpwns.presentation.table.DocumentItemTable;
import com.magnetpwns.printing.InvoicePrint;
import com.magnetpwns.printing.Print;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXDatePicker;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class InvoiceViewDialog extends AbstractInvoiceViewDialog {

    private DocumentItemTableModel deliveryItemTableModel;
    private JTable deliveryItemTable;
    private PaymentTableModel paymentTableModel;
    private JTable paymentTable;
    
    private JLabel unpaidLabel;
    private JButton cancelButton;
    
    private JXDatePicker payDatePicker;
    private JTextField payAmountField;
    private JTextField payLogField;
    private JButton payAddButton;
    
    private Date cancelDate;
    
    private ArrayList<Payment> payments;
    
    public InvoiceViewDialog(Invoice invoice) {
        super(Integer.toString(invoice.getId().getId()) + ": " +
                NbBundle.getMessage(InvoiceViewDialog.class, "CTL_DialogViewInvoice") + invoice.getClient().toString(),
                invoice);
        this.document = invoice;
        deliveryItemTableModel = new DocumentItemTableModel(invoice.getItems());
        paymentTableModel = new PaymentTableModel(invoice.getPayments());
        payments = new ArrayList<Payment>();
    }
    
    private JPanel initPayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 4));
        panel.setBorder(new TitledBorder("Přidat platbu"));
        panel.add(new JLabel("datum"));
        panel.add(new JLabel("částka"));
        panel.add(new JLabel("výpis"));
        panel.add(new JLabel(""));
        panel.add(payDatePicker = new JXDatePicker(new Date(), Format.LOCALE));
        panel.add(payAmountField = new JTextField(document.getUnpaid().toPlainString()));
        panel.add(payLogField = new JTextField());
        panel.add(payAddButton = new JButton(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/ok24.png"))));
        payAddButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = payDatePicker.getDate();
                BigDecimal amount = new BigDecimal(payAmountField.getText());
                String log = payLogField.getText();
                Payment p = new Payment(date, log, amount);
                payments.add(p);
                paymentTableModel.add(p);

                payAmountField.setText("");
                payLogField.setText("");

                updateUnpaid();
            }
        });

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }
    
    private JButton initPrintCancelButton() {
        JButton printCancelButton = new JButton();
        printCancelButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/printcancel24.png")));
        
        printCancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        return printCancelButton;
    }
    private JButton initCancelButton() {
        cancelButton = new JButton();
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/com/magnetpwns/presentation/clear24.png")));

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Opravdu zrušit?", "Zrušit?", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        cancelDate = RequestDialog.showDateInputDialog();
                    ActionEvent ae = new ActionEvent(cancelButton, ActionEvent.ACTION_PERFORMED, "cancel");
                    optionsHandler.actionPerformed(ae);
                    dispose();
                    } catch (CancelException ex) {}
                }
            }
        });

        return cancelButton;
    }
    
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.PAGE_AXIS));
        
        deliveryItemTable = new DocumentItemTable(deliveryItemTableModel);
        paymentTable = new JTable(paymentTableModel);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        toolbar.add(initPrintButton());
        if (!document.isCancelled()) {
            toolbar.add(initCancelButton());
        } else {
            toolbar.add(initPrintCancelButton());
        }
                
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPane.add(toolbar);
        
        innerPane.add(initNoteView());
        
        
        innerPane.add(initIssueDateLabel());
        innerPane.add(initEndDateLabel());
        if (document.isCancelled())
            innerPane.add(initCancelDateLabel());
        
        JPanel splitPane = new JPanel();
        splitPane.setLayout(new BoxLayout(splitPane, BoxLayout.LINE_AXIS));
        splitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(deliveryItemTable);
        //scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        splitPane.add(scrollPane);
        
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
        if (!document.isCancelled()) {
            rightPane.add(initPayPanel());
        }
        
        scrollPane = new JScrollPane(paymentTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPane.add(scrollPane);
        splitPane.add(rightPane);
        
        innerPane.add(splitPane);
        
        innerPane.add(initTotalLabel());
        
        unpaidLabel = new JLabel();
        unpaidLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPane.add(unpaidLabel);
        
        updateUnpaid();
        
        return innerPane;
    }
    
    private void updateUnpaid() {
        BigDecimal newUnpaid = document.getUnpaid();
        for (Payment payment : payments) {
            newUnpaid = newUnpaid.subtract(payment.getAmount());
        }
        
        unpaidLabel.setText("Nezaplaceno: " + newUnpaid.toPlainString());
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == getOkButton()) {
                    try {
                        for (Payment payment : payments) {
                            AquamarinFacade.getDefault().addInvoicePayment(document, payment);
                        }
                    } catch (AquamarinException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    result = true;
                } else if (ae.getSource() == cancelButton) {
                    try {
                        AquamarinFacade.getDefault().cancelInvoice(document, cancelDate);
                    } catch (AquamarinException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        };
    }

    @Override
    protected void printAction() {
        Print.print(new InvoicePrint(document));
    }
    
}
