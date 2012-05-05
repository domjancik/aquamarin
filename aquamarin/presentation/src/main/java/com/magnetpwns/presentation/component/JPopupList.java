/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.presentation.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Collection;

/**
 *
 * @author MagNet
 */
public class JPopupList {

    private int x;
    private int y;
    private Collection items;
    
    int width = 0;
    int height = 0;

    public JPopupList(int x, int y, Collection items) {
        this.x = x;
        this.y = y;
        this.items = items;
    }
    
    public void paint(Graphics g) {
        int[] xPositions = { x, x - 10, x + 10 };
        int[] yPositions = { y, y + 10, y + 10 };
        
        // Min width
        width = 90;
        // Determine maximal text width
        FontMetrics fontMetrics = g.getFontMetrics();
        for (Object o : items) {
            int strWidth = fontMetrics.stringWidth(o.toString());
            if (strWidth > width)
                width = strWidth;
        }
        width += 4;
        // Determine needed height
        int fontHeight = fontMetrics.getHeight();
        height =  fontHeight * items.size() + 6;
        
        Polygon polygon = new Polygon(xPositions, yPositions, 3);
        
        Graphics2D g2d = (Graphics2D) g;
        Color color = new Color(0, 0, 0, 70);
        g2d.setPaint(color);
        g2d.fillRect(x - width / 2, y + 15, width + 10, height + 10);
        color = new Color(0, 184, 228);
        g2d.setPaint(color);
        g2d.fillPolygon(polygon);
        g2d.fillRect(x - width / 2 - 5, y + 10, width + 10, height + 10);
        g2d.setPaint(Color.white);
        g2d.fillRect(x - width / 2, y + 15, width, height);
        g2d.setPaint(Color.black);
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        int line = 1;
        for (Object o : items) {
            g.drawString(o.toString(), x - width / 2 + 2, y + 15 + line++ * fontHeight);
        }
    }
    
    public void clear(Component c) {
        c.repaint(x - width / 2 - 5, y, width + 15, height + 25);
    }
}
