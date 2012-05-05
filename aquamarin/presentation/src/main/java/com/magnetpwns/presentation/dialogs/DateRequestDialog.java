/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs;

import com.magnetpwns.presentation.CancelException;
import java.util.Date;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author MagNet
 */
public class DateRequestDialog extends AbstractDialog {

    private JXDatePicker datePicker;

    public DateRequestDialog() {
        super("Select a date");
    }

    public DateRequestDialog(String title) {
        super(title);
    }
    
    @Override
    protected JPanel innerPane() {
        JPanel innerPane = new JPanel();
        innerPane.add(datePicker = new JXDatePicker(new Date()));
        return innerPane;
    }
    
    public Date getDate() throws CancelException {
        if (show()) {
            return datePicker.getDate();
        } else {
            throw new CancelException();
        }
    }
    
    
    
}
