/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import io.Configuration;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

/**
 *
 * @author Nesh
 */
public class BookFilterDialog extends javax.swing.JDialog {

    /**
     * Creates new form BookFilterDialog
     */
    public BookFilterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleCheckbox = new javax.swing.JCheckBox();
        authorCheckbox = new javax.swing.JCheckBox();
        ISBN10Checkbox = new javax.swing.JCheckBox();
        ISBN13Checkbox = new javax.swing.JCheckBox();
        countCheckbox = new javax.swing.JCheckBox();
        locationCheckbox = new javax.swing.JCheckBox();
        languageCheckbox = new javax.swing.JCheckBox();
        publisherCheckbox = new javax.swing.JCheckBox();
        publishedDateCheckbox = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();
        pageCountCheckbox = new javax.swing.JCheckBox();
        INPbookMaxRowsCount = new javax.swing.JSpinner();
        INPbookMaxRowsCount.setValue(Configuration.getInstance().getMaxBookRowsCount());

        INPorderBy = new javax.swing.JComboBox();
        INPorderType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nastavení zobrazení");

        titleCheckbox.setSelected(true);
        titleCheckbox.setText("Název");
        titleCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleCheckboxActionPerformed(evt);
            }
        });

        authorCheckbox.setSelected(true);
        authorCheckbox.setText("Autor");

        ISBN10Checkbox.setText("ISBN10");

        ISBN13Checkbox.setText("ISBN13");
        ISBN13Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ISBN13CheckboxActionPerformed(evt);
            }
        });

        countCheckbox.setText("Množství skladem");

        locationCheckbox.setText("Umístění");

        languageCheckbox.setText("Jazyk");

        publisherCheckbox.setText("Vydavatel");

        publishedDateCheckbox.setText("Rok vydání");

        okButton.setText("OK");
        okButton.setName("filterConfirm"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        pageCountCheckbox.setText("Počet stránek");

        INPbookMaxRowsCount.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));

        INPorderBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INPorderByActionPerformed(evt);
            }
        });

        INPorderType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INPorderTypeActionPerformed(evt);
            }
        });

        jLabel1.setText("Zobrazit záznamů:");

        jLabel2.setText("Řadit podle:");

        jLabel3.setText("Způsob řazení:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(titleCheckbox)
                                        .addComponent(authorCheckbox)
                                        .addComponent(ISBN10Checkbox)
                                        .addComponent(ISBN13Checkbox))
                                    .addGap(62, 62, 62)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(publishedDateCheckbox)
                                        .addComponent(publisherCheckbox)
                                        .addComponent(languageCheckbox)
                                        .addComponent(locationCheckbox)
                                        .addComponent(pageCountCheckbox)))
                                .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(countCheckbox))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(INPbookMaxRowsCount, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(INPorderBy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(INPorderType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleCheckbox)
                    .addComponent(locationCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(authorCheckbox)
                    .addComponent(languageCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ISBN10Checkbox)
                    .addComponent(publisherCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ISBN13Checkbox)
                    .addComponent(publishedDateCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countCheckbox)
                    .addComponent(pageCountCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPbookMaxRowsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPorderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPorderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void titleCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleCheckboxActionPerformed

    private void ISBN13CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ISBN13CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ISBN13CheckboxActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okButtonActionPerformed

    private void INPorderByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INPorderByActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_INPorderByActionPerformed

    private void INPorderTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INPorderTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_INPorderTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner INPbookMaxRowsCount;
    private javax.swing.JComboBox INPorderBy;
    private javax.swing.JComboBox INPorderType;
    private javax.swing.JCheckBox ISBN10Checkbox;
    private javax.swing.JCheckBox ISBN13Checkbox;
    private javax.swing.JCheckBox authorCheckbox;
    private javax.swing.JCheckBox countCheckbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JCheckBox languageCheckbox;
    private javax.swing.JCheckBox locationCheckbox;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox pageCountCheckbox;
    private javax.swing.JCheckBox publishedDateCheckbox;
    private javax.swing.JCheckBox publisherCheckbox;
    private javax.swing.JCheckBox titleCheckbox;
    // End of variables declaration//GEN-END:variables

    public JCheckBox getISBN10Checkbox() {
        return ISBN10Checkbox;
    }

    public JCheckBox getISBN13Checkbox() {
        return ISBN13Checkbox;
    }

    public JCheckBox getAuthorCheckbox() {
        return authorCheckbox;
    }

    public JCheckBox getCountCheckbox() {
        return countCheckbox;
    }

    public JButton getjButton1() {
        return okButton;
    }

    public JCheckBox getLanguageCheckbox() {
        return languageCheckbox;
    }

    public JCheckBox getLocationCheckbox() {
        return locationCheckbox;
    }

    public JCheckBox getPublishedDateCheckbox() {
        return publishedDateCheckbox;
    }

    public JCheckBox getPublisherCheckbox() {
        return publisherCheckbox;
    }

    public JCheckBox getTitleCheckbox() {
        return titleCheckbox;
    }

    public JCheckBox getPageCountCheckbox() {
        return pageCountCheckbox;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JSpinner getINPbookMaxRowsCount() {
        return INPbookMaxRowsCount;
    }

    public JComboBox getINPorderBy() {
        return INPorderBy;
    }

    public JComboBox getINPorderType() {
        return INPorderType;
    }

}
