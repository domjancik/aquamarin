/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.Client;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collection;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class PostPrint implements Printable {

    private final static int[] COLWIDTHS = {
        5,
        20,
        20,
        8,
        8,
        12 
    }; // Relative sizes of individual columns
    
    private final static int HEADER_HEIGHT = 55; // The header height
    private final static int ROW_HEIGHT = 55; // The height of non header rows
    private final static int ROWS = 10; // Rows to be drawn (not including the header)
    private final static int X_MARGIN = 20; // Left and right margin of the table
    private final static int Y_START = 80; // Top margin of the table
    
    private ArrayList<Client> clients;

    public PostPrint(Collection<Client> clients) {
        this.clients = new ArrayList<Client>(clients);
    }
    
    @Override
    public int print(Graphics grphcs, PageFormat pf, int i) throws PrinterException {
        if (i > 0)
            return NO_SUCH_PAGE;
        
        Graphics2D g2d = (Graphics2D)grphcs;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        String str = NbBundle.getMessage(PostPrint.class, "PostPrint.title");
        GraphicsOperation.drawStringBox(0, 0, (int) pf.getImageableWidth(), 20, str, GraphicsOperation.ALIGN_CENTER, grphcs);
        str = NbBundle.getMessage(PostPrint.class, "PostPrint.top0");
        GraphicsOperation.drawStringBox(X_MARGIN, 20, (int) pf.getImageableWidth(), 20, str, GraphicsOperation.ALIGN_LEFT, grphcs);
        str = NbBundle.getMessage(PostPrint.class, "PostPrint.top1");
        str += " ";
        str += NbPreferences.forModule(OwnerPanel.class).get("title", "");
        str += ", ";
        str += NbPreferences.forModule(OwnerPanel.class).get("address", "");
        str += ", ";
        str += NbPreferences.forModule(OwnerPanel.class).get("city", "");
        GraphicsOperation.drawStringBox(X_MARGIN, 40, (int) pf.getImageableWidth(), 20, str, GraphicsOperation.ALIGN_LEFT, grphcs);
        
        Font defaultFont = grphcs.getFont();
        grphcs.setFont(defaultFont.deriveFont(Font.PLAIN, defaultFont.getSize() - 2));
        str = NbBundle.getMessage(PostPrint.class, "PostPrint.top2");
        grphcs.drawString(str, X_MARGIN + 200, Y_START - grphcs.getFontMetrics().getDescent());
        
        int colWidthSum = 0;
        for (int j : COLWIDTHS) {
            colWidthSum += j;
        }
        double tableWidth = pf.getImageableWidth() - 2 * X_MARGIN;
        double widthRatio = tableWidth / colWidthSum;
        
        int x = X_MARGIN;
        for (int j = 0; j < COLWIDTHS.length; j++) {
            int y = Y_START;
            int colWidth = (int) (COLWIDTHS[j] * widthRatio);
            
            grphcs.setFont(defaultFont.deriveFont(Font.BOLD, defaultFont.getSize() - 2));
            grphcs.drawRect(x, y, colWidth, HEADER_HEIGHT);
            str = NbBundle.getMessage(PostPrint.class, "PostPrint.header" + j);
            GraphicsOperation.drawStringBox(x, y, colWidth, HEADER_HEIGHT, str, GraphicsOperation.ALIGN_CENTER, grphcs);
            grphcs.setFont(defaultFont.deriveFont(Font.PLAIN, defaultFont.getSize() - 2));
            y += HEADER_HEIGHT;
            
            for (int k = 0; k < ROWS; k++) {
                grphcs.drawRect(x, y, colWidth, ROW_HEIGHT);
                if (k < clients.size()) {
                    switch(j) {
                        case 2:
                            Client client = clients.get(k);
                            String name;
                            if (client.getTitleShort().length() > 0) {
                                name = client.getTitleShort();
                            } else {
                                name = client.getTitle();
                            }
                            String[] clientLines = {
                               name,
                               client.getStreet(),
                               client.getCity().getParsedPostcode() + " " + client.getCity().getName()
                            } ;
                            GraphicsOperation.drawStringBox(x, y, colWidth, ROW_HEIGHT, clientLines, GraphicsOperation.ALIGN_LEFT, grphcs);
                            break;
                    }
                }
                y += ROW_HEIGHT;
            }
            
            x += colWidth;
        }
        
        int y = Y_START + HEADER_HEIGHT + ROW_HEIGHT * ROWS + grphcs.getFontMetrics().getHeight();
        str = NbBundle.getMessage(PostPrint.class, "PostPrint.bottom0");
        grphcs.drawString(str, X_MARGIN, y);
        str = NbBundle.getMessage(PostPrint.class, "PostPrint.bottom1");
        grphcs.drawString(str, X_MARGIN + 300, y);
        
        return PAGE_EXISTS;
    }    
}
