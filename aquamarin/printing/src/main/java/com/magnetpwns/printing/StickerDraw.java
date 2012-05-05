/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.model.Client;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 *
 * @author MagNet
 */
public class StickerDraw {
    
    private Graphics2D g;
    private int width;
    private int height;
    private int padding;
    private int circleRadius;
    private Client client;
    private int index;
    
    private int line;

    public StickerDraw(Graphics g, int width, int height, int padding, int circleRadius, Client client, int index) {
        this.g = (Graphics2D) g;
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.circleRadius = circleRadius;
        this.client = client;
        this.index = index;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public void draw(int x, int y, int fontSize, int textPadding, double scale, boolean aa, boolean outline) {
        g.setTransform(AffineTransform.getScaleInstance(scale, scale));
        if (aa)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        draw(x, y, fontSize, textPadding, outline);
    }
        
    public void draw(int x, int y, int fontSize, int textPadding, boolean outline) {
        if (outline)
            g.drawRect(x, y, width, height);
        line = 0;
        
        Font defaultFont = g.getFont();
        g.setFont(defaultFont.deriveFont(defaultFont.getStyle(), defaultFont.getSize2D() + fontSize));
        
        String name;
        if (client.getTitleShort().length() > 0) {
            name = client.getTitleShort();
        } else {
            name = client.getTitle();
        }
        drawStringLine(x, y + textPadding, name);
        drawStringLine(x, y + textPadding, client.getStreet());
        drawStringLine(x, y + textPadding, client.getCity().getParsedPostcode() + " " + client.getCity().getName());

        g.setFont(defaultFont);
        
        int ovalPosX = x + width - circleRadius - padding;
        int ovalPosY = y + padding;
        String text = Integer.toString(index);
        int textWidth = g.getFontMetrics().stringWidth(text);
        int fontAscent = g.getFontMetrics().getAscent();
        g.drawOval(ovalPosX, ovalPosY, circleRadius, circleRadius);
        g.drawString(text, ovalPosX + circleRadius / 2 - textWidth / 2, ovalPosY + circleRadius / 2 + fontAscent / 2);
    }
    
    private void drawStringLine(int x, int y, String str) {
        int fontHeight = g.getFontMetrics().getHeight();
        g.drawString(str, x + padding, y + padding + (line + 1) * fontHeight);
        line++;
    }
    
}
