/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import javax.swing.JOptionPane;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class MessageDialog {
    public static void showNoResultAlertDialog() {
        String noResultString = NbBundle.getMessage(MessageDialog.class, "CTL_NoResult");
        JOptionPane.showMessageDialog(null, noResultString);
    }
}
