/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.Invoice;
import com.magnetpwns.model.PaymentType;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public abstract class AbstractDialog {

    private JButton okButton;
    private JButton cancelButton;
    
    private String title;
    
    protected boolean result = false;
    
    protected ActionListener optionsHandler;
    
    private Dialog d;
    
    public AbstractDialog(String title) {
        this.title = title;
    }
    

    abstract protected JPanel innerPane();
    
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
    
    public boolean show() {
        optionsHandler = createOptionsHandler();
        DialogDescriptor dde = new DialogDescriptor(innerPane(), title, true, getOptions(), this, DialogDescriptor.DEFAULT_ALIGN, HelpCtx.DEFAULT_HELP, optionsHandler);
        dde.setClosingOptions(getClosingOption());
        d = DialogDisplayer.getDefault().createDialog(dde);
        d.setVisible(true);
        return result;
    }
    
    protected void dispose() {
        d.dispose();
    }
    
    protected JButton[] getClosingOption() {
        return getOptions();
    }
    
    protected JButton[] getOptions() {
        return new JButton[] {
            getOkButton(),
            getCancelButton()
        };
    }
    
     public JButton getCancelButton() {
        if (cancelButton == null)
            cancelButton = new JButton(NbBundle.getMessage(AbstractDialog.class, "CTL_Cancel"));
        return cancelButton;
    }

    public JButton getOkButton() {
        if (okButton == null)
            okButton = new JButton(NbBundle.getMessage(AbstractDialog.class, "CTL_Ok"));
        return okButton;
    }
    
    protected void clear(JTextField field) {
        field.setText("");
    }
    
}
