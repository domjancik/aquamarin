/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author MagNet
 */
public class Print {
    public static void print(Printable p) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(p);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
            /* The job did not successfully complete */
            }
        }
    }
}
