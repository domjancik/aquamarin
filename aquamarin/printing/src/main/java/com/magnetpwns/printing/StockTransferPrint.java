/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;

/**
 *
 * @author MagNet
 */
public class StockTransferPrint extends AbstractDocumentPrint<Document<DocumentItem>> {

    private StockTransfer transfer;
    
    public StockTransferPrint(StockTransfer transfer) {
        this.transfer = transfer;
        List<DocumentItem> items = new ArrayList<DocumentItem>();
        for (CountedProduct cp  : transfer.getItems()) {
            items.add(new DocumentItem(cp.getProduct(), cp.getAmount(), BigDecimal.ZERO, Client.DUMMY));
        }
        Document<DocumentItem> d = new Document<DocumentItem>(DocumentId.ZERO,
                Client.DUMMY,
                transfer.getDate(), items, transfer.getNote(), "");
        document = d;
    }

    @Override
    protected String getId() {
        return "";
    }
    
    
    @Override
    protected String[] getSupplierLabel() {
        return new String[] {
            "\\sDodavatel:",
            "\\t" + transfer.getSourceStock().getTitle(),
            transfer.getSourceStock().getStreet(),
            transfer.getSourceStock().getCity().toPostString(),
            "",
            "\\bKsg: " + transfer.getSourceStock().getId().getId()
        };
    }
    
    @Override
    protected String[] getSupplierInfo() {
        return new String[] {            
        };
    }
    
    @Override
    protected String[] getClientLabel() {
        Client c = document.getClient();
        return new String[] {
            "\\sOdběratel:",
            "\\t" + transfer.getDestinationStock().getTitle(),
            transfer.getDestinationStock().getStreet(),
            transfer.getDestinationStock().getCity().toPostString(),
            "",
            "\\bKsg: " + transfer.getDestinationStock().getId().getId()
        };
    }
    
    @Override
    protected String[] getClientInfo() {
        return new String[] {
        };
    }
    
    @Override
    protected String getName() {
        return "Dodací list do konsignačního skladu";
    }

    @Override
    protected String getTotalName() {
        return "Dodáno celkem za";
    }

    @Override
    protected String getBottomTotal() {
        return "Celkem včetně DPH";
    }

    @Override
    protected String[] getTopInfoLabel() {
        return new String[0];
    }
    
    @Override
    protected String[] getTopInfoInfo() {
        return new String[0];
    }

    @Override
    protected String getSupplierBottomline() {
        return "";
    }    

    @Override
    protected String getItemHeader() {
        return "Dodáváme vám následující položky";
    }

    @Override
    protected String[] getInfoLabels() {
        return new String[] {
            NbBundle.getMessage(getClass(), "DocumentPrint.dateIssue")
        };
    }

    @Override
    protected String[] getInfo() {
        DateFormat df = getDateFormat();
        
        return new String[] {
            df.format(document.getIssueDate()),
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
    protected int getColumnCount() {
        return 6;
    }
    
}
