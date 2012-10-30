/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.printing;

import org.openide.util.NbPreferences;

final class OwnerPanel extends javax.swing.JPanel {

    private final OwnerOptionsPanelController controller;

    OwnerPanel(OwnerOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        cityField = new javax.swing.JTextField();
        icoField = new javax.swing.JTextField();
        dicField = new javax.swing.JTextField();
        bankField = new javax.swing.JTextField();
        ibanField = new javax.swing.JTextField();
        subtitleField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel6.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel7.text")); // NOI18N

        titleField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.titleField.text")); // NOI18N

        addressField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.addressField.text")); // NOI18N

        cityField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.cityField.text")); // NOI18N

        icoField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.icoField.text")); // NOI18N

        dicField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.dicField.text")); // NOI18N

        bankField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.bankField.text")); // NOI18N

        ibanField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.ibanField.text")); // NOI18N

        subtitleField.setText(org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.subtitleField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(OwnerPanel.class, "OwnerPanel.jLabel8.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subtitleField)
                    .addComponent(titleField)
                    .addComponent(addressField)
                    .addComponent(cityField)
                    .addComponent(icoField)
                    .addComponent(dicField)
                    .addComponent(bankField)
                    .addComponent(ibanField, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(subtitleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(icoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(dicField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(bankField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(ibanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(163, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        titleField.setText(NbPreferences.forModule(OwnerPanel.class).get("title", ""));
        subtitleField.setText(NbPreferences.forModule(OwnerPanel.class).get("subtitle", ""));
        addressField.setText(NbPreferences.forModule(OwnerPanel.class).get("address", ""));
        cityField.setText(NbPreferences.forModule(OwnerPanel.class).get("city", ""));
        icoField.setText(NbPreferences.forModule(OwnerPanel.class).get("ico", ""));
        dicField.setText(NbPreferences.forModule(OwnerPanel.class).get("dic", ""));
        bankField.setText(NbPreferences.forModule(OwnerPanel.class).get("bank", ""));
        ibanField.setText(NbPreferences.forModule(OwnerPanel.class).get("iban", ""));
    }

    void store() {
        NbPreferences.forModule(OwnerPanel.class).put("title", titleField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("subtitle", subtitleField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("address", addressField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("city", cityField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("ico", icoField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("dic", dicField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("bank", bankField.getText());
        NbPreferences.forModule(OwnerPanel.class).put("iban", ibanField.getText());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JTextField bankField;
    private javax.swing.JTextField cityField;
    private javax.swing.JTextField dicField;
    private javax.swing.JTextField ibanField;
    private javax.swing.JTextField icoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField subtitleField;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables
}
