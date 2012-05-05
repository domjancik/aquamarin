/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
//        Product p = new Product(null, "asdxx", null, "Swift 70+", null, new BigDecimal(100), BigDecimal.ZERO,
//                new Manufacturer(null, "Oticon"), new TaxRate(new TaxRateId(1), null, new BigDecimal(14)),
//                null);
//        
//        DocumentItem di = new DocumentItem(p, 5, new BigDecimal(5), null);
//        ArrayList<DocumentItem> items = new ArrayList<DocumentItem>();
//        items.add(di);
//        items.add(di);
//        items.add(di);
//        
//        Date d = new Date();
//        Client c = new Client(null, "Hello world", null, null, true,
//                "Address 1024", 12345, "CZ3456789", null, null, null, null, null,
//                new City(null, 12345, "City test", null), BigDecimal.ZERO, true);
//        Invoice i = new Invoice(null, c, d, items, "Hello", "Dodano", d, PaymentType.TRANSFER, null, new BigDecimal(100), null, 12);
//        
//        PrinterJob job = PrinterJob.getPrinterJob();
//                job.setPrintable(new InvoicePrint(i));
//                boolean ok = job.printDialog();
//                if (ok) {
//                    try {
//                        job.print();
//                    } catch (PrinterException ex) {
//                    /* The job did not successfully complete */
//                    }
//                }
    }
}
