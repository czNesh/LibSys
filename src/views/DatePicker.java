/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JComponent;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;

/**
 *
 * @author Nesh
 */
public class DatePicker extends javax.swing.JDialog {

    JDatePanel panel;

    /**
     * Creates new form DatePicker
     */
    public DatePicker(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        //initComponents();

        // SET PANEL
        panel = JDateComponentFactory.createJDatePanel();
        panel.setShowYearButtons(true);
        panel.addActionListener(new DateAction());
        getContentPane().add((JComponent) panel);
        setSize(250, 250);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public Date getDate() {
        return new Date(panel.getModel().getYear()-1900,panel.getModel().getMonth(),panel.getModel().getDay());
    }

    private class DateAction implements ActionListener {

        public DateAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
  
}
