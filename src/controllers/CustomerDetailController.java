/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import models.BorrowTableModel;
import models.entity.Borrow;
import models.entity.Customer;
import services.BorrowService;
import views.CustomerDetailDialog;

/**
 *
 * @author petr.hejhal
 */
public class CustomerDetailController extends BaseController {

    private CustomerDetailDialog dialog;
    Customer customer;
    BorrowTableModel tableModel;

    public CustomerDetailController(Customer customer) {
        this.customer = customer;
        dialog = new CustomerDetailDialog(null, true);
        initListeners();
        updateView();
        showView();
    }

    private void initListeners() {
        CustomerDetailActionListener a = new CustomerDetailActionListener();
        dialog.getCloseButton().addActionListener(a);
        dialog.getEditButton().addActionListener(a);
        dialog.getPrntButton().addActionListener(a);
        dialog.getBarcodeButton().addActionListener(a);
        dialog.getQrcodeButton().addActionListener(a);

        CustomerDetailMouseListener m = new CustomerDetailMouseListener();
        dialog.getBorrowTable().addMouseListener(m);
    }

    private void updateView() {
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfCustomer(customer.getId()));
        dialog.getBorrowTable().setModel(tableModel);

        dialog.getInputSSN().setText(String.valueOf(customer.getSSN()));
        dialog.getInputFName().setText(customer.getFirstName());
        dialog.getInputLName().setText(customer.getLastName());
        dialog.getInputAdress().setText(customer.getFullAdress());
        dialog.getInputEmail().setText(customer.getEmail());
        dialog.getInputPhone().setText(customer.getPhone());
        dialog.getInputNotes().setText(customer.getNotes());
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

    private class CustomerDetailActionListener implements ActionListener {

        public CustomerDetailActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "editButton":
                    break;
                case "closeButton":
                    dialog.dispose();
                    dialog = null;
                    break;
                case "printButton":
                    break;
                case "barcodeButton":
                    break;
                case "qrcodeButton":
                    break;

                default:
                    break;
            }

        }
    }

    private class CustomerDetailMouseListener implements MouseListener {

        public CustomerDetailMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(dialog.getBorrowTable().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
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
