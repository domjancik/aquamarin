/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.model.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public abstract class AbstractDocumentPrint<T extends Document> implements Printable {

    private final static int MARGIN = 20;
    private final static int IDBOX_HEIGHT = 20;
    private final static int CLIENTBOX_Y = 100;
    private final static int CLIENTBOX_HEIGHT = 120;
    private final static int SUPBOX_Y = 0;
    private final static int SUPBOX_HEIGHT = 143;
    private final static int SPACE = 10;
    
    private double pageWidth = 0;
    private Map<TaxRateId, BigDecimal> taxRateTotals;
    
    protected T document;

    /**
     * Public argument-less constructor for use with extending classes,
     * those should make sure to set the document variable themselves.
     */
    public AbstractDocumentPrint() {
    }
    
    public AbstractDocumentPrint(T document) {
        this.document = document;
    }
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0)
            return NO_SUCH_PAGE;
        
        taxRateTotals = new HashMap<TaxRateId, BigDecimal>();
        
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY() + MARGIN);
        pageWidth = pageFormat.getWidth() - MARGIN * 2;
        int halfWidth = (int) pageWidth / 2;
        
        int yLeft = 0;
        int yRight = 0;
        
        drawSupplierBox(graphics, 0, SUPBOX_Y, halfWidth - 5, SUPBOX_HEIGHT);
        
        yLeft += SUPBOX_Y + SUPBOX_HEIGHT + SPACE;
        
        yLeft += drawInfo(graphics, 0, yLeft).height;
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(halfWidth, 0, halfWidth, IDBOX_HEIGHT);
        g2d.setColor(Color.BLACK);
        
        GraphicsOperation.drawStringBox(halfWidth, 0, halfWidth, IDBOX_HEIGHT, getName(), GraphicsOperation.ALIGN_LEFT, g2d);
        GraphicsOperation.drawStringBox(halfWidth, 0, halfWidth, IDBOX_HEIGHT, "\\b" + getId(), GraphicsOperation.ALIGN_RIGHT, g2d);
        yRight += IDBOX_HEIGHT + SPACE;
        
        Dimension d = GraphicsOperation.drawMultiline(halfWidth + SPACE, yRight, getTopInfoLabel(), graphics);
        GraphicsOperation.drawMultiline(d.width + halfWidth + SPACE, yRight, getTopInfoInfo(), graphics);
        
        drawClientBox(graphics, halfWidth, CLIENTBOX_Y, halfWidth, CLIENTBOX_HEIGHT);
        yRight = CLIENTBOX_Y + CLIENTBOX_HEIGHT;
        
        int yMax = yRight;
        if (yLeft > yRight)
            yMax = yLeft;
        yMax += SPACE;
        
        Font defFont =  graphics.getFont();
        graphics.setFont(defFont.deriveFont(Font.PLAIN, defFont.getSize() - 2));
        
        drawLine(graphics, yMax);
        yMax += GraphicsOperation.drawMultiline(0, yMax, new String[] {"\\b" + getItemHeader()}, graphics).height;
        drawLine(graphics, yMax);
        
        yMax += GraphicsOperation.drawTable(getRowLabels(), getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        drawLine(graphics, yMax);
        
        // Items
        for (DocumentItem di : (Collection<DocumentItem>) document.getItems()) {
            yMax += GraphicsOperation.drawTable(getRow(di), getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
            drawLine(graphics, yMax);
            BigDecimal old = taxRateTotals.put(di.getProduct().getTaxRate().getId(), di.getDiscountedTotal());
            if (old != null) {
                taxRateTotals.put(di.getProduct().getTaxRate().getId(), di.getDiscountedTotal().add(old));
            }
        }
        
        yMax += 5;
        drawLine(graphics, yMax);
        
        Collection<TaxRate> taxRates = AquamarinFacade.getDefault().findAllTaxRates();
        
        String[] row1Data = new String[getColumnCount()];
        String[] row2Data = new String[getColumnCount()];
        String[] row3Data = new String[getColumnCount()];
        String[] row4Data = new String[getColumnCount()];
        String[] row5Data = new String[getColumnCount()];
        for (int i = 0; i < row1Data.length; i++) {
            row1Data[i] = "";
            row2Data[i] = "";
            row3Data[i] = "";
            row4Data[i] = "";
            row5Data[i] = "";
        }
        
        row1Data[0] = "\\b" + getTotalName();
        row2Data[0] = "Základ DPH";
        row3Data[0] = "Částka DPH";
        
        BigDecimal taxTotal = BigDecimal.ZERO;
        
        int i = 2;
        for (TaxRate taxRate : taxRates) {
            row1Data[i] = "\\b" + taxRate.toPercentString();
            BigDecimal taxRateTotal = taxRateTotals.get(taxRate.getId());
            if (taxRateTotal == null)
                taxRateTotal = BigDecimal.ZERO;
            taxRateTotal = taxRateTotal.setScale(2);
            row2Data[i] = taxRateTotal.toPlainString();
            BigDecimal tax = taxRateTotal.multiply(taxRate.getTaxOnlyMultiplication());
            row3Data[i] = tax.setScale(2,RoundingMode.HALF_UP).toPlainString();
            taxTotal = taxTotal.add(tax);
            
            i++;
        }
        
        taxTotal = taxTotal.setScale(2, RoundingMode.HALF_UP);
        
        row1Data[getColumnCount() - 2] = "\\b" + "Celkem";
        row2Data[getColumnCount() - 2] = document.getDiscountedTotal().toPlainString();
        row3Data[getColumnCount() - 2] = taxTotal.toPlainString();
        row2Data[getColumnCount() - 1] = "CZK";
        row3Data[getColumnCount() - 1] = "CZK";
        
        yMax += GraphicsOperation.drawTable(row1Data, getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        drawLine(graphics, yMax);
        yMax += GraphicsOperation.drawTable(row2Data, getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        yMax += GraphicsOperation.drawTable(row3Data, getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        drawLine(graphics, yMax);
        
        row4Data[0] = "\\bZaokrouhlení";
        row5Data[0] = "\\t" + getBottomTotal();
        
        BigDecimal roundingDifference = document.getDiscountedTaxedTotalRounded()
                .subtract(taxTotal.add(document.getDiscountedTotal())).setScale(2);
        row4Data[getColumnCount() - 2] = roundingDifference.toPlainString();
        row5Data[getColumnCount() - 2] = "\\t" + document.getDiscountedTaxedTotalRounded().toPlainString();
        row4Data[getColumnCount() - 1] = "CZK";
        row5Data[getColumnCount() - 1] = "\\tCZK";
        
        yMax += GraphicsOperation.drawTable(row4Data, getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        drawLine(graphics, yMax);
        yMax += SPACE;
        
        graphics.setFont(defFont);
        
        yMax += GraphicsOperation.drawTable(row5Data, getRowSizes(), getRowAlignments(), 0, yMax, pageWidth, graphics);
        yMax += SPACE;
        drawLine(graphics, yMax);
        
        yMax += SPACE;
        yMax += drawNote(graphics, yMax, (int) pageWidth);
        
        yMax += SPACE;
        GraphicsOperation.drawMultiline(halfWidth, yMax, new String[] {"Podpis a razítko:"}, graphics);
        
        return PAGE_EXISTS;
    }
    
    public static final int RECT_ROUNDNESS = 10;
    public static final int TEXT_PADDING = 10;
    
    protected String[] getTopInfoLabel() {
        return new String[] {
            "Konstantní symbol:",
            "Variabilní symbol:",
        };
    }
    
    protected String[] getTopInfoInfo() {
        return new String[] {
            "0308",
            "\\b" + getId(),
        };
    }
    
    protected String getBottomTotal() {
        return "Celkem k úhradě včetně DPH";
    }
    
    private int drawNote(Graphics g, int y, int width) {
        Dimension d = GraphicsOperation.drawMultiline(0 + TEXT_PADDING, y + TEXT_PADDING,
                new String[] {"Poznámka: " + document.getNote()}, g);
        int height = d.height + TEXT_PADDING * 2;
        g.drawRoundRect(0, y, width, height, RECT_ROUNDNESS, RECT_ROUNDNESS);
        return height;
    }
    
    protected String[] getSupplierLabel() {
        return new String[] {
            "\\sDodavatel:",
            "\\t",
            "",
            "",
            "",
            "\\sBankovní spojení:",
            "\\sIBAN:"
        };
    }
    
    protected String[] getSupplierInfo() {
        return new String[] {
            "\\sIČ: " + NbPreferences.forModule(OwnerPanel.class).get("ico", "") + " DIČ: " + NbPreferences.forModule(OwnerPanel.class).get("dic", ""),
            "\\t" + NbPreferences.forModule(OwnerPanel.class).get("title", ""),
            NbPreferences.forModule(OwnerPanel.class).get("subtitle", ""),
            NbPreferences.forModule(OwnerPanel.class).get("address", ""),
            NbPreferences.forModule(OwnerPanel.class).get("city", ""),
//            "",
            "\\S" + NbPreferences.forModule(OwnerPanel.class).get("bank", ""),
            "\\S" + NbPreferences.forModule(OwnerPanel.class).get("iban", "")
        };
    }
    
    protected String getSupplierBottomline() {
        return "\\sZapsán v ŽR Ob. ŽÚ Praha JM";
    }
     
    private void drawSupplierBox(Graphics g, int x, int y, int width, int height) {
        
        String[] label = getSupplierLabel();
        String[] info = getSupplierInfo();
        String bottomLine = getSupplierBottomline();
        
        Dimension d = GraphicsOperation.drawMultiline(x + TEXT_PADDING, y + TEXT_PADDING, label, g);
        GraphicsOperation.drawMultiline(x + TEXT_PADDING * 2 + d.width, y + TEXT_PADDING, info, g);
        GraphicsOperation.drawMultiline(x + TEXT_PADDING, d.height + TEXT_PADDING, new String[] {bottomLine}, g);
        
        g.drawRoundRect(x, y, width, height, RECT_ROUNDNESS, RECT_ROUNDNESS);
    }
    
    protected String[] getClientLabel() {
        Client c = document.getClient();
        return new String[] {
            "\\SOdběratel:",
            "\\S",
            "\\t" + c.getTitle(),
            c.getStreet(),
            c.getCity().getParsedPostcode() + " " + c.getCity().getName()
        };
    }
    
    protected String[] getClientInfo() {
        Client c = document.getClient();
        return new String[] {
            "\\SIČ: " + Integer.toString(c.getIco()),
            "\\SDIČ: " + c.getDic()
        };
    }
    
    private void drawClientBox(Graphics g, int x, int y, int width, int height) {
        String[] label = getClientLabel();
        String[] info = getClientInfo();
        
        GraphicsOperation.drawMultiline(x + TEXT_PADDING, y + TEXT_PADDING, label, g);
        GraphicsOperation.drawMultiline(x + width / 2, y + TEXT_PADDING, info, g);
        
        g.drawRoundRect(x, y, width, height, RECT_ROUNDNESS, RECT_ROUNDNESS);
    }
    
    private Dimension drawInfo(Graphics g, int x, int y) {
        String[] extraInfoLabels = getInfoLabels();
        String[] extraInfo = getInfo();
        
        Dimension d = GraphicsOperation.drawMultiline(x, y, extraInfoLabels, g);
        Dimension d2 = GraphicsOperation.drawMultiline(x + d.width + TEXT_PADDING, y, extraInfo, g);
        
        return new Dimension(d.width + TEXT_PADDING + d2.width, d.height);
    }
    
    private void drawLine(Graphics g, int y) {
        g.drawLine(0, y, (int) pageWidth, y);
    }
    
    protected DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
    }
    
    abstract protected String getName();
    abstract protected String getTotalName();
    abstract protected String getItemHeader();
    abstract protected String[] getInfoLabels();
    abstract protected String[] getInfo();
    abstract protected String[] getRowLabels();
    abstract protected double[] getRowSizes();
    abstract protected int[] getRowAlignments();
    abstract protected String[] getRow(DocumentItem di);
    abstract protected int getColumnCount();
    
    protected String getId() {
        return Integer.toString(document.getSysId());
    }
    
}
