/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.Client;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class StickerPrint implements Printable {
    
    private ArrayList<Client> clients;

    public StickerPrint(Collection<Client> clients) {
        this.clients = new ArrayList<Client>(clients);
    }
    
    @Override
    public int print(Graphics g, PageFormat pf, int i) throws PrinterException {
        int cols = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerCols", 2);
        int rows = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerRows", 8);
        
        int stickersPerPage = cols * rows;
        int startIndex = i * stickersPerPage;
        if (startIndex >= clients.size()) {
            return NO_SUCH_PAGE;
        }
 
        int width = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerWidth", 200);
        int height = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerHeight", 100);
        int padding = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerPadding", 10);
        int textPadding = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerTextPadding", 0);
        int circleRadius = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerRadius", 25);
        int fontSize = NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerFontSize", 0);
        
        
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        /* Now we perform our rendering */
        int row = 0;
        int col = 0;
        int index = 1;
        for (int j = startIndex; j < startIndex + stickersPerPage; j++) {
            if (j >= clients.size())
                break;
            Client client = clients.get(j);
            new StickerDraw(g, width, height, padding, circleRadius, client, index).draw(col * width, row * height, fontSize, textPadding, false);
            index++;
            col++;
            if (col >= cols) {
                col = 0;
                row ++;
                if (row >= rows)
                    break;
            }
        }
        
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
}
