/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author MagNet
 */
public class GraphicsOperation {
    
    public final static int ALIGN_LEFT = 0;
    public final static int ALIGN_CENTER = 1;
    public final static int ALIGN_RIGHT = 2;
    
    private final static int PADDING = 5;
    
    public static int drawStringBox(int x, int y, int width, String str, int alignment, Graphics g) {
        String[] lines = str.split("\n");
        return drawStringBox(x, y, width, 0, lines, alignment, false, g);
    }
    
    public static int drawStringBox(int x, int y, int width, int height, String str, int alignment, Graphics g) {
        String[] lines = str.split("\n");
        return drawStringBox(x, y, width, height, lines, alignment, true, g);
    }
    
    public static int drawStringBox(int x, int y, int width, int height, String[] lines, int alignment, Graphics g) {
        return drawStringBox(x, y, width, height, lines, alignment, true, g);
    }
    
    public static int drawStringBox(int x, int y, int width, int height, String[] lines, int alignment, boolean valign, Graphics g) {
       int fontHeight = g.getFontMetrics().getHeight();
       int stringHeight = lines.length * fontHeight;
       Font defFont = g.getFont();
       
       switch (alignment) {
           case ALIGN_LEFT:
               x += PADDING;
               break;
           case ALIGN_CENTER:
               x += width / 2;
               break;
           case ALIGN_RIGHT:
               x += width - PADDING;
               break;
       }
       
       if (valign)
           y += height / 2 - stringHeight / 2;
       y += g.getFontMetrics().getAscent();
       
       for (String string : lines) {
           string = prepareString(string, defFont, g);
           
           int lineWidth = g.getFontMetrics().stringWidth(string);
           fontHeight = g.getFontMetrics().getHeight();
           
           switch (alignment) {
               case ALIGN_CENTER:
                   g.drawString(string, x - lineWidth / 2, y);
                   break;
               case ALIGN_RIGHT:
                   g.drawString(string, x - lineWidth, y);
                   break;
               case ALIGN_LEFT:
                   g.drawString(string, x, y);
                   break;
           }
           
           y += fontHeight;
       }
       
       g.setFont(defFont);
       return stringHeight;
    }
    
    public static int drawTable(String[] data, double[] sizes, int[] alignments, int x, int y, double width, Graphics g) {
        int maxHeight = 0;
        
        double sizeSum = 0;
        for (double d : sizes) {
            sizeSum += d;
        }
        double ratio = width / sizeSum;
        for (int i = 0; i < sizes.length; i++)
            sizes[i] *= ratio;
        
        for (int i = 0; i < data.length; i++) {
            int height = drawStringBox(x, y, (int) sizes[i], data[i], alignments[i], g);
            if (height > maxHeight)
                maxHeight = height;
            x += sizes[i];
        }
        
        return maxHeight;
    }
    
    public static Dimension drawMultiline(int x, int y, String[] lines, Graphics g) {
        int maxWidth = 0;
        Font defFont = g.getFont();
        int totalHeight = 0;
        
        for (String string : lines) {
            
            string = prepareString(string, defFont, g);
            
            int fontAscent = g.getFontMetrics().getAscent();
            int fontBelow = g.getFontMetrics().getDescent() + g.getFontMetrics().getLeading();
            
            y += fontAscent;
            
            int lineWidth = g.getFontMetrics().stringWidth(string);
            if (lineWidth > maxWidth)
                maxWidth = lineWidth;
            g.drawString(string, x, y);
            y += fontBelow;
            totalHeight += fontAscent + fontBelow;
        }
        
        g.setFont(defFont);
        return new Dimension(maxWidth, totalHeight);
    }
    
    enum FontStyle {
        REGULAR,
        BOLD,
        TITLE,
        SMALL,
        SMALLBOLD
    }
    
    private static FontStyle checkStyle(String s) {        
        if (s.matches("^\\\\S.*"))
            return FontStyle.SMALLBOLD;
        if (s.matches("^\\\\s.*"))
            return FontStyle.SMALL;
        if (s.matches("^\\\\b.*"))
            return FontStyle.BOLD;
        if (s.matches("^\\\\t.*"))
            return FontStyle.TITLE;
        
        
        return FontStyle.REGULAR;
    }
    
    private static Font styleFont(Font f, FontStyle fs) {
        switch (fs) {
                case REGULAR:
                    return f;
                case BOLD:
                    return f.deriveFont(Font.BOLD);
                case TITLE:
                    return f.deriveFont(Font.BOLD, f.getSize() + 2);
                case SMALL:
                    return f.deriveFont(Font.PLAIN, f.getSize() - 2);
                case SMALLBOLD:
                    return f.deriveFont(Font.BOLD, f.getSize() - 2);
                default:
                    return f;
            }
    }
    
    private static String prepareString(String s, Font defFont, Graphics g) {
        FontStyle fs = checkStyle(s);
        g.setFont(styleFont(defFont, fs));
        if (fs != FontStyle.REGULAR)
            s = s.substring(2);
        
        return s;
    }
}
