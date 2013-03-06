/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 *
 * @author Nesh
 */
public class BorrowFilterDialog extends javax.swing.JDialog {

    /**
     * Creates new form BorrowFilterDialog
     */
    public BorrowFilterDialog(java.awt.Frame parent, boolean modal) {
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

        okButton = new javax.swing.JButton();
        customerCheckBox = new javax.swing.JCheckBox();
        librarianCheckBox = new javax.swing.JCheckBox();
        fromCheckBox = new javax.swing.JCheckBox();
        toCheckBox = new javax.swing.JCheckBox();
        returnedCheckBox = new javax.swing.JCheckBox();
        itemCheckBox = new javax.swing.JCheckBox();
        groupCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nastavení zobrazení");

        okButton.setText("OK");
        okButton.setName("filterConfirm"); // NOI18N

        customerCheckBox.setSelected(true);
        customerCheckBox.setText("Zákazník");

        librarianCheckBox.setText("Knihovník");

        fromCheckBox.setSelected(true);
        fromCheckBox.setText("Datum od");

        toCheckBox.setSelected(true);
        toCheckBox.setText("Datum do");

        returnedCheckBox.setSelected(true);
        returnedCheckBox.setText("Vráceno");

        itemCheckBox.setSelected(true);
        itemCheckBox.setText("Kniha");

        groupCheckBox.setText("Spojit výpůjčky jednoho uživatele");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerCheckBox)
                            .addComponent(itemCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(librarianCheckBox)
                            .addComponent(returnedCheckBox)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(groupCheckBox)
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(fromCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(toCheckBox))
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerCheckBox)
                    .addComponent(returnedCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(librarianCheckBox)
                    .addComponent(itemCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fromCheckBox)
                    .addComponent(toCheckBox))
                .addGap(26, 26, 26)
                .addComponent(groupCheckBox)
                .addGap(8, 8, 8)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox customerCheckBox;
    private javax.swing.JCheckBox fromCheckBox;
    private javax.swing.JCheckBox groupCheckBox;
    private javax.swing.JCheckBox itemCheckBox;
    private javax.swing.JCheckBox librarianCheckBox;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox returnedCheckBox;
    private javax.swing.JCheckBox toCheckBox;
    // End of variables declaration//GEN-END:variables

    public JButton getOkButton() {
        return okButton;
    }

    public JCheckBox getCustomerCheckBox() {
        return customerCheckBox;
    }

    public JCheckBox getFromCheckBox() {
        return fromCheckBox;
    }

    public JCheckBox getLibrarianCheckBox() {
        return librarianCheckBox;
    }

    public JCheckBox getReturnedCheckBox() {
        return returnedCheckBox;
    }

    public JCheckBox getToCheckBox() {
        return toCheckBox;
    }

    public JCheckBox getItemCheckBox() {
        return itemCheckBox;
    }

    public JCheckBox getGroupCheckBox() {
        return groupCheckBox;
    }
    

}
