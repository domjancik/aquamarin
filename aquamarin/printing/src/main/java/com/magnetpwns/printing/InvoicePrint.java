/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.DocumentItem;
import com.magnetpwns.model.Invoice;
import java.math.BigDecimal;
import java.text.DateFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class InvoicePrint extends AbstractDocumentPrint<Invoice> {

    boolean hasDiscount;
    
    public InvoicePrint(Invoice document) {
        super(document);
        
        hasDiscount = false;
        for (DocumentItem i : document.getItems()) {
            if (i.getDiscount().compareTo(BigDecimal.ZERO) != 0) {
                hasDiscount = true;
                break;
            }
        }
    }

    @Override
    protected String getName() {
        return "Faktura - Daňový doklad";
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
        if (hasDiscount)
            return new String[] {
                "Označení\nPopis dodávky",
                "Kód",
                "Ks",
                "DPH",
                "Cena ks CZK\nCena ks s DPH CZK",
                "Celkem CZK\nCelkem s DPH CZK",
                "Sleva",
            };
        else
            return new String[] {
                "Označení\nPopis dodávky",
                "Kód",
                "Ks",
                "DPH",
                "Cena ks CZK\nCena ks s DPH CZK",
                "Celkem CZK\nCelkem s DPH CZK",
            };
            
    }

    @Override
    protected String[] getRow(DocumentItem di) {
        if (hasDiscount)
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
        else
            return new String[] {
                di.getProduct().toString(),
                di.getProduct().getCode(),
                Integer.toString(di.getAmount()),
                di.getProduct().getTaxRate().toPercentString(),
                di.getDiscountedProductPrice().toPlainString() + "\n"
                    + di.getDiscountedTaxedProductPrice().toPlainString(),
                di.getDiscountedTotal().toPlainString() + "\n"
                    + di.getDiscountedTaxedTotal().toPlainString(),
            };
    }

    @Override
    protected double[] getRowSizes() {
        if (hasDiscount)
            return new double[] {
                1.5,
                0.7,
                0.5,
                0.7,
                1.2,
                1.2,
                0.5
            };
        else
            return new double[] {
                1.5,
                0.7,
                0.5,
                0.7,
                1.2,
                1.2
            };
    }

    @Override
    protected int[] getRowAlignments() {
        if (hasDiscount)
            return new int[] {
                GraphicsOperation.ALIGN_LEFT,
                GraphicsOperation.ALIGN_LEFT,
                GraphicsOperation.ALIGN_RIGHT,
                GraphicsOperation.ALIGN_RIGHT,
                GraphicsOperation.ALIGN_RIGHT,
                GraphicsOperation.ALIGN_RIGHT,
                GraphicsOperation.ALIGN_RIGHT
            };
        else
            return new int[] {
                GraphicsOperation.ALIGN_LEFT,
                GraphicsOperation.ALIGN_LEFT,
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
        return hasDiscount ? 7 : 6;
    }

    @Override
    protected String getItemHeader() {
        return hasDiscount ?
                "Fakturujeme vám následující položky (v cenách je již obsažena uvedená sleva):" :
                "Fakturujeme vám následující položky:";
    }
}
