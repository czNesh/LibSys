/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import models.CustomerTableModel;
import models.entity.Customer;
import views.CustomerFilterDialog;
import views.MainView;

/**
 *
 * @author petr.hejhal
 */
public class CustomerTabController {

    MainView mainView;
    CustomerTableModel tableModel;
    CustomerFilterDialog filterDialog;

    CustomerTabController(MainView mainView) {
        this.mainView = mainView;

        tableModel = new CustomerTableModel();
        mainView.getCustomerTable().setModel(tableModel);

        filterDialog = new CustomerFilterDialog(null, true);

        initListeners();
        updateView();
    }

    private void initListeners() {
        CustomerTabMouseListener m = new CustomerTabMouseListener();
        mainView.getCustomerTable().addMouseListener(m);

        CustomerTabActionListener a = new CustomerTabActionListener();
        mainView.getCustomerFilterButton().addActionListener(a);
        mainView.getCustomerTableNextButton().addActionListener(a);
        mainView.getCustomerTablePrevButton().addActionListener(a);

        CustomerTabKeyListener k = new CustomerTabKeyListener();
        mainView.getCustomerTableInputNumber().addKeyListener(k);
        mainView.getCustomerFilterInput().addKeyListener(k);
    }

    public void updateView() {
        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();
        
        // Update page counting 
        mainView.getCustomerTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getCustomerTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));
    }

    private class CustomerTabKeyListener implements KeyListener {

        public CustomerTabKeyListener() {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "customerFilter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getCustomerFilterInput().getText().trim().isEmpty()) {
                            tableModel.resetFilter();
                        } else {
                            tableModel.applyFilter(mainView.getBookFilterInput().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "customerPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getBookTableInputNumber().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class CustomerTabActionListener implements ActionListener {

        public CustomerTabActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
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
                    updateView();
                    filterDialog.setVisible(false);
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
                    break;
            }

        }
    }

    private class CustomerTabMouseListener implements MouseListener {

        public CustomerTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2){
                Customer temp = tableModel.getCustomer(mainView.getCustomerTable().getSelectedRow());
                CustomerDetailController c = new CustomerDetailController(temp);
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
}
