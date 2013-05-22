/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Nesh
 */
public class SystemUserDetailDialog extends javax.swing.JDialog {

    /**
     * Creates new form SystemUserDetailDialog
     */
    public SystemUserDetailDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        getRootPane().setDefaultButton(BTNcancel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        INPfirstName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        INPlastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        INPlogin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        INPemail = new javax.swing.JTextField();
        INPmaster = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        BTNcancel = new javax.swing.JButton();
        BTNdelete = new javax.swing.JButton();
        BTNrenew = new javax.swing.JButton();
        BTNedit = new javax.swing.JButton();
        INPpassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detail uživatele");
        setResizable(false);

        jLabel1.setText("Jméno:");

        INPfirstName.setEditable(false);
        INPfirstName.setNextFocusableComponent(INPlastName);

        jLabel2.setText("Přijmení:");

        INPlastName.setEditable(false);
        INPlastName.setNextFocusableComponent(INPlogin);

        jLabel3.setText("Login:");

        INPlogin.setEditable(false);
        INPlogin.setNextFocusableComponent(INPpassword);

        jLabel4.setText("Heslo:");

        jLabel5.setText("Email:");

        INPemail.setEditable(false);
        INPemail.setNextFocusableComponent(INPmaster);

        INPmaster.setText("Správce:");
        INPmaster.setEnabled(false);
        INPmaster.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        INPmaster.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        INPmaster.setIconTextGap(120);
        INPmaster.setNextFocusableComponent(BTNedit);

        BTNcancel.setText("Zavřít");
        BTNcancel.setName("cancel"); // NOI18N
        BTNcancel.setNextFocusableComponent(INPfirstName);

        BTNdelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/trash-can-delete.png"))); // NOI18N
        BTNdelete.setToolTipText("Smazat uživatele");
        BTNdelete.setName("delete"); // NOI18N

        BTNrenew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check-2-icon.png"))); // NOI18N
        BTNrenew.setToolTipText("Obnovit uživatele");
        BTNrenew.setName("renew"); // NOI18N

        BTNedit.setText("Upravit");
        BTNedit.setName("edit"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNedit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BTNcancel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 35, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BTNdelete, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BTNrenew, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BTNedit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BTNcancel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BTNrenew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(BTNdelete)
                .addContainerGap())
        );

        INPpassword.setEditable(false);
        INPpassword.setNextFocusableComponent(INPemail);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(INPfirstName)
                            .addComponent(INPlastName)
                            .addComponent(INPlogin)
                            .addComponent(INPemail, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(INPpassword)))
                    .addComponent(INPmaster, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(INPfirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(INPlastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(INPlogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(INPpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(INPemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(INPmaster)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNcancel;
    private javax.swing.JButton BTNdelete;
    private javax.swing.JButton BTNedit;
    private javax.swing.JButton BTNrenew;
    private javax.swing.JTextField INPemail;
    private javax.swing.JTextField INPfirstName;
    private javax.swing.JTextField INPlastName;
    private javax.swing.JTextField INPlogin;
    private javax.swing.JCheckBox INPmaster;
    private javax.swing.JPasswordField INPpassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    public JButton getBTNcancel() {
        return BTNcancel;
    }

    public JButton getBTNedit() {
        return BTNedit;
    }

    public JTextField getINPemail() {
        return INPemail;
    }

    public JTextField getINPfirstName() {
        return INPfirstName;
    }

    public JTextField getINPlastName() {
        return INPlastName;
    }

    public JTextField getINPlogin() {
        return INPlogin;
    }

    public JCheckBox getINPmaster() {
        return INPmaster;
    }

    public JPasswordField getINPpassword() {
        return INPpassword;
    }

    public JButton getBTNdelete() {
        return BTNdelete;
    }

    public JButton getBTNrenew() {
        return BTNrenew;
    }
}