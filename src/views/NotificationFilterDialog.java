/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;

/**
 *
 * @author Nesh
 */
public class NotificationFilterDialog extends javax.swing.JDialog {

    /**
     * Creates new form NotificationFilterDialog
     */
    public NotificationFilterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(BTNok);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        INPmaxRows = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        INPfrom = new javax.swing.JCheckBox();
        INPto = new javax.swing.JCheckBox();
        INPitem = new javax.swing.JCheckBox();
        INPtype = new javax.swing.JCheckBox();
        INPcustomer = new javax.swing.JCheckBox();
        INPborrowCode = new javax.swing.JCheckBox();
        BTNok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nastavení zobrazení");
        setResizable(false);

        INPmaxRows.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));

        jLabel1.setText("Zobrazit záznamů:");

        INPfrom.setSelected(true);
        INPfrom.setText("Datum od");

        INPto.setSelected(true);
        INPto.setText("Datum do");

        INPitem.setText("Kniha");

        INPtype.setSelected(true);
        INPtype.setText("Typ oznámení");

        INPcustomer.setSelected(true);
        INPcustomer.setText("Zákazník");

        INPborrowCode.setSelected(true);
        INPborrowCode.setText("Číslo půjčky");

        BTNok.setText("OK");
        BTNok.setName("filterConfirmed"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BTNok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(14, 14, 14)
                        .addComponent(INPmaxRows))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(INPfrom)
                            .addComponent(INPborrowCode)
                            .addComponent(INPcustomer))
                        .addGap(18, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(INPto)
                            .addComponent(INPitem)
                            .addComponent(INPtype))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPcustomer)
                    .addComponent(INPtype))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPitem)
                    .addComponent(INPborrowCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPfrom)
                    .addComponent(INPto))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(INPmaxRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(BTNok)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNok;
    private javax.swing.JCheckBox INPborrowCode;
    private javax.swing.JCheckBox INPcustomer;
    private javax.swing.JCheckBox INPfrom;
    private javax.swing.JCheckBox INPitem;
    private javax.swing.JSpinner INPmaxRows;
    private javax.swing.JCheckBox INPto;
    private javax.swing.JCheckBox INPtype;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    public JButton getBTNok() {
        return BTNok;
    }

    public JCheckBox getINPborrowCode() {
        return INPborrowCode;
    }

    public JCheckBox getINPcustomer() {
        return INPcustomer;
    }

    public JCheckBox getINPfrom() {
        return INPfrom;
    }

    public JCheckBox getINPitem() {
        return INPitem;
    }

    public JSpinner getINPmaxRows() {
        return INPmaxRows;
    }

    public JCheckBox getINPto() {
        return INPto;
    }

    public JCheckBox getINPtype() {
        return INPtype;
    }
}
