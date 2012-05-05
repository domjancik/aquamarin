/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.Delivery;
import com.magnetpwns.model.DocumentItem;
import java.text.DateFormat;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class DeliveryPrint extends AbstractDocumentPrint<Delivery> {

    public DeliveryPrint(Delivery document) {
        super(document);
    }

    @Override
    protected String getName() {
        return "Dodací list";
    }

    @Override
    protected String[] getInfoLabels() {
        return new String[] {
            NbBundle.getMessage(getClass(), "DocumentPrint.dateIssue"),
            "",
            NbBundle.getMessage(getClass(), "DocumentPrint.noteDelivery"),
        };
    }

    @Override
    protected String[] getInfo() {
        DateFormat df = getDateFormat();
        
        return new String[] {
            "\\b" + df.format(document.getIssueDate()),
            "",
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
            "Celkem CZK\nCelkem s DPH CZK"
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
                + di.getDiscountedTaxedTotal().toPlainString()
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
            1.2
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
            GraphicsOperation.ALIGN_RIGHT
        };
    }

    @Override
    protected String getTotalName() {
        return "Zboží celkem";
    }

    @Override
    protected int getColumnCount() {
        return 6;
    }

    @Override
    protected String getItemHeader() {
        return "Dodáváme vám následující položky (ceny jsou uvedeny v plné výši bez slev):";
    }
}
