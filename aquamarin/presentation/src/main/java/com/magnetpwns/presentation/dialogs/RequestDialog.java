/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.presentation.CancelException;
import java.math.BigDecimal;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author MagNet
 */
public class RequestDialog {
    public static int showIntegerInputDialog() throws CancelException {
        while (true) {
            try {
                String result = JOptionPane.showInputDialog("Kolik?");
                if (result == null)
                    throw new CancelException();
                int amount = Integer.parseInt(result);
                if (amount > 0) {
                    return amount;
                }
            } catch (NumberFormatException e) {
                // Disregard
            }
            JOptionPane.showMessageDialog(null, "Použijte prosím celé nezáporné číslo");
        }
    }
    
    public static double showDoubleInputDialog() throws CancelException {
        while (true) {
            try {
                String result = JOptionPane.showInputDialog("Kolik?");
                if (result == null)
                    throw new CancelException();
                double amount = Double.parseDouble(result);
                if (amount > 0) {
                    return amount;
                }
            } catch (NumberFormatException e) {
                // Disregard
            }
            JOptionPane.showMessageDialog(null, "Použijte prosím nezáporné číslo");
        }
    }
    
    public static BigDecimal showBigDecimalInputDialog() throws CancelException {
        while (true) {
            try {
                String result = JOptionPane.showInputDialog("Kolik?");
                if (result == null)
                    throw new CancelException();
                BigDecimal amount = new BigDecimal(result);
                if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                    return amount;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Zadejte číslo (používejte deset. tečku)");
                continue;
            }
            JOptionPane.showMessageDialog(null, "Použijte prosím nezáporné číslo");
        }
    }
    
    public static Date showDateInputDialog() throws CancelException {
        return new DateRequestDialog().getDate();
    }
}
