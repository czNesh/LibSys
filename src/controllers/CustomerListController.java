/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import models.CustomerTableModel;
import views.CustomerFilterDialog;
import views.CustomerListDialog;

/**
 *
 * @author Nesh
 */
public class CustomerListController extends BaseController {

    CustomerListDialog dialog;
    CustomerTableModel tableModel;
    CustomerFilterDialog filterDialog;

    public CustomerListController(JFrame parent) {
        dialog = new CustomerListDialog(parent, true);
        tableModel = new CustomerTableModel();
        updateData();
        dialog.getResultTable().setModel(tableModel);
        filterDialog = new CustomerFilterDialog(null, true);
        initListeners();
    }

    private void initListeners() {
        // BUTTONs LISTENER
        ButtonsListener b = new ButtonsListener();
        dialog.getConfirmButton().addActionListener(b);
        dialog.getCancelButton().addActionListener(b);
        dialog.getFilterButton().addActionListener(b);

        // FIELDs LISTENER
        dialog.getInputCID().addKeyListener(new KeyPressedListener());
    }

    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    private void updateData() {
        tableModel.updateData();
        tableModel.fireTableDataChanged();
    }

    private class KeyPressedListener implements KeyListener {

        public KeyPressedListener() {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            updateData();
        }
    }

    private class ButtonsListener implements ActionListener {

        public ButtonsListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            String buttonName = b.getName();

            switch (buttonName) {
                case "confirm":

                    break;
                case "cancel":
                    dispose();
                    break;

                case "filter":
                    filterDialog.getFilterOKButton().addActionListener(this);
                    filterDialog.setLocationRelativeTo(null);
                    filterDialog.setVisible(true);
                    break;

                case "filterConfirmed":
                    System.out.println("1");
                    tableModel.setVisibility(
                            filterDialog.getCIDCheckBox().isSelected(),
                            filterDialog.getNameCheckBox().isSelected(),
                            filterDialog.getStreetCheckBox().isSelected(),
                            filterDialog.getCityCheckBox().isSelected(),
                            filterDialog.getCountryCheckBox().isSelected(),
                            filterDialog.getEmailCheckBox().isSelected(),
                            filterDialog.getPhoneCheckBox().isSelected());
                    tableModel.fireTableStructureChanged();
                    filterDialog.setVisible(false);
                    break;
                default:
                    System.out.println("Chyba - Jmeno polozky neodpovida zadne operaci (Buttonlistener)");
            }

        }
    }
}
