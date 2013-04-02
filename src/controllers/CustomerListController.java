/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
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
    Customer customer;
    boolean selectionMode;

    public CustomerListController(JFrame parent, boolean selectionMode) {
        dialog = new CustomerListDialog(parent, true);
        this.selectionMode = selectionMode;
        tableModel = new CustomerTableModel();
        updateView();
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
        dialog.getSearchButton().addActionListener(b);
        dialog.getBookTableNextButton().addActionListener(b);
        dialog.getBookTablePrevButton().addActionListener(b);

        // KeyListener
        CustomerListKeyListener k = new CustomerListKeyListener();
        dialog.getInputSSN().addKeyListener(k);
        dialog.getInputFName().addKeyListener(k);
        dialog.getInputLName().addKeyListener(k);
        dialog.getInputEmail().addKeyListener(k);
        dialog.getInputPhone().addKeyListener(k);

        // MouseListener
        CustomerMouseListener m = new CustomerMouseListener();
        dialog.getResultTable().addMouseListener(m);
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

    private void updateView() {
        tableModel.updateData();
        tableModel.fireTableDataChanged();

        // Update page counting 
        dialog.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        dialog.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));
    }

    public Customer getSeletedCustomer() {
        return customer;
    }

    private class CustomerMouseListener implements MouseListener {

        public CustomerMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();
                JTable t = dialog.getResultTable();
                customer = (Customer) tableModel.getCustomer(t.getSelectedRow());
            }
            if (selectionMode) {
                if (customer != null) {
                    dialog.dispose();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    // LISTENER CLASSes
    private class CustomerListButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            String buttonName = b.getName();

            switch (buttonName) {
                case "confirm":
                    if (dialog.getResultTable().getSelectedRow() != -1 && selectionMode) {
                        customer = tableModel.getCustomer(dialog.getResultTable().getSelectedRow());
                        dialog.dispose();
                    }

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

                case "search":
                    tableModel.setFilter(
                            dialog.getInputSSN().getText().trim(),
                            dialog.getInputFName().getText().trim(),
                            dialog.getInputLName().getText().trim(),
                            dialog.getInputEmail().getText().trim(),
                            dialog.getInputPhone().getText().trim());
                    tableModel.fireTableDataChanged();
                    break;
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "prevPage":
                    tableModel.prevPage();
                    updateView();
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

            // ESCAPE pri stisku sipky
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
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
}