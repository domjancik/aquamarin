/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import com.magnetpwns.component.JTextFieldNumberDocument;
import com.magnetpwns.model.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import org.openide.util.NbPreferences;

final class StickerlayoutPanel extends javax.swing.JPanel {

    private final StickerlayoutOptionsPanelController controller;

    StickerlayoutPanel(StickerlayoutOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        widthSlider = new javax.swing.JSlider();
        previewTopPane = new javax.swing.JPanel();
        previewPane = new javax.swing.JPanel();
        colsField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rowsField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        heightSlider = new javax.swing.JSlider();
        previewButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        paddingSlider = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        radiusSlider = new javax.swing.JSlider();
        adaptButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        fontSizeSlider = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        textPaddingSlider = new javax.swing.JSlider();

        widthSlider.setMajorTickSpacing(50);
        widthSlider.setMaximum(400);
        widthSlider.setMinimum(100);
        widthSlider.setMinorTickSpacing(10);
        widthSlider.setPaintLabels(true);
        widthSlider.setPaintTicks(true);

        previewTopPane.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.previewTopPane.border.title"))); // NOI18N

        previewPane.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout previewPaneLayout = new javax.swing.GroupLayout(previewPane);
        previewPane.setLayout(previewPaneLayout);
        previewPaneLayout.setHorizontalGroup(
            previewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );
        previewPaneLayout.setVerticalGroup(
            previewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout previewTopPaneLayout = new javax.swing.GroupLayout(previewTopPane);
        previewTopPane.setLayout(previewTopPaneLayout);
        previewTopPaneLayout.setHorizontalGroup(
            previewTopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(previewTopPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(previewPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        previewTopPaneLayout.setVerticalGroup(
            previewTopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, previewTopPaneLayout.createSequentialGroup()
                .addComponent(previewPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        colsField.setDocument(new JTextFieldNumberDocument());
        colsField.setText(org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.colsField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel2.text")); // NOI18N

        rowsField.setDocument(new JTextFieldNumberDocument());
        rowsField.setText(org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.rowsField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel4.text")); // NOI18N

        heightSlider.setMajorTickSpacing(20);
        heightSlider.setMaximum(190);
        heightSlider.setMinimum(50);
        heightSlider.setMinorTickSpacing(5);
        heightSlider.setPaintLabels(true);
        heightSlider.setPaintTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(previewButton, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.previewButton.text")); // NOI18N
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel5.text")); // NOI18N

        paddingSlider.setMajorTickSpacing(20);
        paddingSlider.setMinorTickSpacing(5);
        paddingSlider.setPaintLabels(true);
        paddingSlider.setPaintTicks(true);
        paddingSlider.setSnapToTicks(true);
        paddingSlider.setValue(10);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel6.text")); // NOI18N

        radiusSlider.setMaximum(40);
        radiusSlider.setMinimum(10);
        radiusSlider.setMinorTickSpacing(5);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setSnapToTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(adaptButton, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.adaptButton.text")); // NOI18N
        adaptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adaptButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel7.text")); // NOI18N

        fontSizeSlider.setMajorTickSpacing(5);
        fontSizeSlider.setMaximum(10);
        fontSizeSlider.setMinimum(-10);
        fontSizeSlider.setPaintLabels(true);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setValue(0);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(StickerlayoutPanel.class, "StickerlayoutPanel.jLabel8.text")); // NOI18N

        textPaddingSlider.setMajorTickSpacing(20);
        textPaddingSlider.setMinorTickSpacing(5);
        textPaddingSlider.setPaintLabels(true);
        textPaddingSlider.setPaintTicks(true);
        textPaddingSlider.setSnapToTicks(true);
        textPaddingSlider.setValue(0);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(previewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(colsField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rowsField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(adaptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel3)
                        .addComponent(widthSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addComponent(heightSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addComponent(paddingSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(radiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(fontSizeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8)
                    .addComponent(textPaddingSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(previewTopPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(previewTopPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(colsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(rowsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adaptButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(widthSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heightSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paddingSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textPaddingSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSizeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(previewButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
        int width = widthSlider.getValue();
        int height = heightSlider.getValue();
        int padding = paddingSlider.getValue();
        int textPadding = textPaddingSlider.getValue();
        int radius = radiusSlider.getValue();
        int cols = Integer.parseInt(colsField.getText());
        int rows = Integer.parseInt(rowsField.getText());
        int fontSize = fontSizeSlider.getValue();
        
        Client testClient = new Client(new ClientId(0),
                "MUDr. Jan Novák",
                "",
                "",
                true,
                "Korunní 24",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                new City(new CityId(0), 12345, "Novákov",
                new Country(null, "Česká Republika")),
                BigDecimal.ZERO,
                false);
        Graphics2D g2d = (Graphics2D) previewPane.getGraphics();
        g2d.setPaint(Color.white);
        g2d.fillRect(0, 0, previewPane.getWidth(), previewPane.getHeight());
        
        g2d.transform(AffineTransform.getScaleInstance(0.5, 0.5));
        
        int index = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                new StickerDraw(previewPane.getGraphics(), width, height, padding, radius, testClient, index++).draw(j * width, i * height, fontSize, textPadding, 0.5, true, true);
            }
        }
        
        g2d.setPaint(Color.gray);
        int pageWidth = (int) NbPreferences.forModule(GeneralPanel.class).getDouble("lastPageWidth", 0);
        int pageHeight = (int) NbPreferences.forModule(GeneralPanel.class).getDouble("lastPageHeight", 0);
        g2d.drawLine(pageWidth, 0, pageWidth, pageHeight);
        g2d.drawLine(0, pageHeight, pageWidth, pageHeight);
    }//GEN-LAST:event_previewButtonActionPerformed

    private void adaptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adaptButtonActionPerformed
        double pageWidth = NbPreferences.forModule(GeneralPanel.class).getDouble("lastPageWidth", 0);
        double pageHeight = NbPreferences.forModule(GeneralPanel.class).getDouble("lastPageHeight", 0);
        int stickerWidth = (int) (pageWidth / Integer.parseInt(colsField.getText()));
        int stickerHeight = (int) (pageHeight / Integer.parseInt(rowsField.getText()));
        widthSlider.setValue(stickerWidth);
        heightSlider.setValue(stickerHeight);
                
    }//GEN-LAST:event_adaptButtonActionPerformed

    void load() {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(StickerlayoutPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        // someCheckBox.setSelected(NbPreferences.forModule(StickerlayoutPanel.class).getBoolean("someFlag", false));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
        
        colsField.setText(Integer.toString(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerCols", 2)));
        rowsField.setText(Integer.toString(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerRows", 8)));
        widthSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerWidth", 200));
        heightSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerHeight", 100));
        paddingSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerPadding", 10));
        textPaddingSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerTextPadding", 0));
        radiusSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerRadius", 25));
        fontSizeSlider.setValue(NbPreferences.forModule(StickerlayoutPanel.class).getInt("stickerFontSize", 0));
    }

    void store() {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(StickerlayoutPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        // NbPreferences.forModule(StickerlayoutPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
        
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerCols", Integer.parseInt(colsField.getText()));
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerRows", Integer.parseInt(rowsField.getText()));
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerWidth", widthSlider.getValue());
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerHeight", heightSlider.getValue());
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerPadding", paddingSlider.getValue());
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerTextPadding", textPaddingSlider.getValue());
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerRadius", radiusSlider.getValue());
        NbPreferences.forModule(StickerlayoutPanel.class).putInt("stickerFontSize", fontSizeSlider.getValue());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adaptButton;
    private javax.swing.JTextField colsField;
    private javax.swing.JSlider fontSizeSlider;
    private javax.swing.JSlider heightSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSlider paddingSlider;
    private javax.swing.JButton previewButton;
    private javax.swing.JPanel previewPane;
    private javax.swing.JPanel previewTopPane;
    private javax.swing.JSlider radiusSlider;
    private javax.swing.JTextField rowsField;
    private javax.swing.JSlider textPaddingSlider;
    private javax.swing.JSlider widthSlider;
    // End of variables declaration//GEN-END:variables
}