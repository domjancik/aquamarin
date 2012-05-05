/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.format.Format;
import com.magnetpwns.model.ProformaInvoice;
import javax.swing.JLabel;

/**
 *
 * @author MagNet
 */
public abstract class AbstractProformaViewDialog<T extends ProformaInvoice> extends AbstractViewDialog<T> {

    public AbstractProformaViewDialog(String title, T document) {
        super(title, document);
    }
    
    protected JLabel initEndDateLabel() {
        return createLeftAlignedLabel("Datum (s)platnosti: " + Format.DATE_FORMAT.format(document.getEndDate()));
    }
}
