/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class CalibrationPrint implements Printable {

    private static final int GRIDSPACE = 10;
    private static final int BLACK_GRIDLINE = 10;
    
    @Override
    public int print(Graphics grphcs, PageFormat pf, int i) throws PrinterException {
        if (i > 0)
            return NO_SUCH_PAGE;
        
        NbPreferences.forModule(GeneralPanel.class).putDouble("lastPageWidth", pf.getImageableWidth());
        NbPreferences.forModule(GeneralPanel.class).putDouble("lastPageHeight", pf.getImageableHeight());
        
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        int fontHeight = grphcs.getFontMetrics().getHeight();
        
        // Draw vertical lines
        for (int j = 0; j * GRIDSPACE < pf.getImageableWidth(); j++) {
            if (j % BLACK_GRIDLINE == 0) {
                g2d.setPaint(Color.black);
                g2d.drawString(Integer.toString(j * GRIDSPACE), j * GRIDSPACE, fontHeight);
            } else {
                g2d.setPaint(Color.gray);
            }
            
            g2d.drawLine(j * GRIDSPACE, 0, j * GRIDSPACE, (int) pf.getImageableHeight());
        }
        
        // Draw horizontal lines
        for (int j = 0; j * GRIDSPACE < pf.getImageableHeight(); j++) {
            if (j % BLACK_GRIDLINE == 0) {
                g2d.setPaint(Color.black);
                g2d.drawString(Integer.toString(j * GRIDSPACE), 5, j * GRIDSPACE);
            } else {
                g2d.setPaint(Color.gray);
            }
            
            g2d.drawLine(0, j * GRIDSPACE, (int) pf.getImageableWidth(), j * GRIDSPACE);
        }
        
        return PAGE_EXISTS;
    }
    
}
