/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.Invoice;
import java.text.DateFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class ProformaPrint extends AbstractDocumentPrint<Invoice> {

    public ProformaPrint(Invoice document) {
        super(document);
    }

    @Override
    protected String getName() {
        return "Proforma faktura";
    }

    @Override
    protected String[] getInfoLabels() {
        return new String[] {
            NbBundle.getMessage(getClass(), "DocumentPrint.dateIssue"),
            NbBundle.getMessage(getClass(), "DocumentPrint.datePay"),
            NbBundle.getMessage(getClass(), "DocumentPrint.dateTax"),
            "",
            NbBundle.getMessage(getClass(), "DocumentPrint.paymentType"),
            NbBundle.getMessage(getClass(), "DocumentPrint.noteDelivery"),
        };
    }

    @Override
    protected String[] getInfo() {
        DateFormat df = getDateFormat();
        
        return new String[] {
            df.format(document.getIssueDate()),
            "\\b" + df.format(document.getEndDate()),
            df.format(document.getIssueDate()),
            "",
            "\\b" + document.getPaymentType().toString(),
            document.getDeliveryNote()
        };
    }

    @Override
    protected String[] getRowLabels() {
        return new String[] {
            "Označení\nPopis dodávky",
            "Kód",
            "Ks",
            "DPH",
            "Cena ks CZK\nCena ks s DPH CZK",
            "Celkem CZK\nCelkem s DPH CZK",
            "Sleva",
        };
    }

    @Override
    protected String[] getRow(DocumentItem di) {
        return new String[] {
            di.getProduct().toString(),
            di.getProduct().getCode(),
            Integer.toString(di.getAmount()),
            di.getProduct().getTaxRate().toPercentString(),
            di.getDiscountedProductPrice().toPlainString() + "\n"
                + di.getDiscountedTaxedProductPrice().toPlainString(),
            di.getDiscountedTotal().toPlainString() + "\n"
                + di.getDiscountedTaxedTotal().toPlainString(),
            di.getDiscountPercentString(),
        };
    }

    @Override
    protected double[] getRowSizes() {
        return new double[] {
            1.5,
            0.7,
            0.5,
            0.7,
            1.2,
            1.2,
            0.5
        };
    }

    @Override
    protected int[] getRowAlignments() {
        return new int[] {
            GraphicsOperation.ALIGN_LEFT,
            GraphicsOperation.ALIGN_LEFT,
            GraphicsOperation.ALIGN_RIGHT,
            GraphicsOperation.ALIGN_RIGHT,
            GraphicsOperation.ALIGN_RIGHT,
            GraphicsOperation.ALIGN_RIGHT,
            GraphicsOperation.ALIGN_RIGHT
        };
    }

    @Override
    protected String getTotalName() {
        return "Faktura celkem";
    }

    @Override
    protected int getColumnCount() {
        return 7;
    }

    @Override
    protected String getItemHeader() {
        return "Nabízíme vám následující položky (v cenách je již obsažena uvedená sleva):";
    }
}
