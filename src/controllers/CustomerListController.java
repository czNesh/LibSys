/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import models.CustomerTableModel;
import models.entity.Customer;
import services.CustomerService;
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
        // ActionListener
        CustomerListButtonListener b = new CustomerListButtonListener();
        dialog.getConfirmButton().addActionListener(b);
        dialog.getCancelButton().addActionListener(b);
        dialog.getFilterButton().addActionListener(b);

        // KeyListener
        CustomerListKeyListener k = new CustomerListKeyListener();
        dialog.getInputSSN().addKeyListener(k);
        dialog.getInputFName().addKeyListener(k);
        dialog.getInputLName().addKeyListener(k);

        // FocusListener
        CustomerListFocusListener f = new CustomerListFocusListener();
        dialog.getInputSSN().addFocusListener(f);
        dialog.getInputFName().addFocusListener(f);
        dialog.getInputLName().addFocusListener(f);
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

    
    // LISTENER CLASSes
    
    private class CustomerListButtonListener implements ActionListener {

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
                            filterDialog.getSSNCheckBox().isSelected(),
                            filterDialog.getNameCheckBox().isSelected(),
                            filterDialog.getStreetCheckBox().isSelected(),
                            filterDialog.getCityCheckBox().isSelected(),
                            filterDialog.getCountryCheckBox().isSelected(),
                            filterDialog.getEmailCheckBox().isSelected(),
                            filterDialog.getPhoneCheckBox().isSelected(),
                            filterDialog.getNotesCheckBox().isSelected());
                    tableModel.fireTableStructureChanged();
                    filterDialog.setVisible(false);
                    break;
                default:
                    System.out.println("Chyba - Jmeno polozky neodpovida zadne operaci (Buttonlistener)");
            }

        }
    }

    private class CustomerListKeyListener implements KeyListener {

        List<Customer> customers;

        public CustomerListKeyListener() {
            customers = CustomerService.getInstance().getCustomers();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // pokud se nezapise znak - hned skonci
            if (String.valueOf(e.getKeyChar()).trim().isEmpty()) {
                return;
            }

            // priprava promennych 
            String in;
            int start;

            // co se doplňuje 
            switch (((JComponent) e.getSource()).getName()) {

                // doplnění jména
                case "fname":
                    in = dialog.getInputFName().getText().trim();
                    start = in.length();

                    // zadany aspon 2 znaky
                    if (start > 1) {
                        for (Customer c : customers) {
                            if (c.getFirstName().startsWith(in)) {
                                dialog.getInputFName().setText(c.getFirstName());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputFName().setSelectionStart(start);
                    dialog.getInputFName().setSelectionEnd(dialog.getInputFName().getText().length());
                    break;

                // doplnění přijmení    
                case "lname":
                    in = dialog.getInputLName().getText().trim();
                    start = in.length();

                    // zadany aspon 2 znaky
                    if (start > 1) {
                        for (Customer c : customers) {
                            if (c.getLastName().startsWith(in)) {
                                dialog.getInputLName().setText(c.getLastName());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputLName().setSelectionStart(start);
                    dialog.getInputLName().setSelectionEnd(dialog.getInputLName().getText().length());
                    break;

                case "ssn":
                    in = dialog.getInputSSN().getText().trim();
                    start = in.length();

                    // zadany aspon 4 znaky
                    if (start > 3) {
                        for (Customer c : customers) {
                            if (String.valueOf(c.getSSN()).startsWith(in)) {
                                dialog.getInputSSN().setText(String.valueOf(c.getSSN()));
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputSSN().setSelectionStart(start);
                    dialog.getInputSSN().setSelectionEnd(dialog.getInputSSN().getText().length());



                    break;
                default:
                    break;

            }
        }
    }

    private class CustomerListFocusListener implements FocusListener {

        @Override
        public void focusLost(FocusEvent e) {
            updateData();
        }

        @Override
        public void focusGained(FocusEvent e) {
            // NOT TO DO
        }
    }
}
