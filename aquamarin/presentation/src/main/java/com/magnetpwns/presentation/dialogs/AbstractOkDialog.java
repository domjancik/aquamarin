/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;

/**
 *
 * @author MagNet
 */
public abstract class AbstractOkDialog extends AbstractDialog {

    public AbstractOkDialog(String title) {
        super(title);
    }
    
    @Override
    protected ActionListener createOptionsHandler() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
    }
    
    @Override
    protected JButton[] getOptions() {
        return new JButton[] {
            getOkButton(),
        };
    }

}
