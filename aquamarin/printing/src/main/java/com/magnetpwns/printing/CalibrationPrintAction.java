/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Print",
id = "com.magnetpwns.printing.CalibrationPrintAction")
@ActionRegistration(displayName = "#CTL_CalibrationPrintAction")
@ActionReferences({})
@Messages("CTL_CalibrationPrintAction=Calibration page print")
public final class CalibrationPrintAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        PrinterJob job = PrinterJob.getPrinterJob();
         job.setPrintable(new CalibrationPrint());
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
