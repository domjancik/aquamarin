/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.dialogs.view;

import com.magnetpwns.format.Format;
import com.magnetpwns.model.Invoice;
import javax.swing.JLabel;

/**
 *
 * @author MagNet
 */
public abstract class AbstractInvoiceViewDialog extends AbstractProformaViewDialog<Invoice> {

    public AbstractInvoiceViewDialog(String title, Invoice document) {
        super(title, document);
    }
    
    protected JLabel initCancelDateLabel() {
        return createLeftAlignedLabel("Datum zrušení: " + Format.DATE_FORMAT.format(document.getCancelledDate()));
    }
}
