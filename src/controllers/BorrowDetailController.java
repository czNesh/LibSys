/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import models.BookTableModel;
import models.entity.Borrow;
import services.BorrowService;
import views.BorrowDetailDialog;

/**
 *
 * @author Nesh
 */
public class BorrowDetailController extends BaseController {

    private BorrowDetailDialog dialog;
    private Borrow item;
    private BookTableModel tableModel;

    public BorrowDetailController(Borrow item) {
        this.item = item;
        dialog = new BorrowDetailDialog(null, true);
        tableModel = new BookTableModel(BorrowService.getInstance().getBooksOfBorrow(item.getBorrowCode()));
        updateView();
        initListeners();
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
        dialog.getCustomerSSNTextField().setText(String.valueOf(item.getCustomer().getSSN()));
        dialog.getCustomerNameTextField().setText(item.getCustomer().getFullName());
        dialog.getCustomerAdressTextField().setText(item.getCustomer().getFullAdress());
        dialog.getCustomerPhoneTextField().setText(item.getCustomer().getPhone());
        dialog.getCustomerEmailTextField().setText(item.getCustomer().getEmail());
        dialog.getCustomerNotesTextArea().setText(item.getCustomer().getNotes());

        dialog.getBookListTable().setModel(tableModel);
    }

    private void initListeners() {
        BorrowDialogActionListener a = new BorrowDialogActionListener();
        dialog.getOkButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);
    }

    private class BorrowDialogActionListener implements ActionListener {

        public BorrowDialogActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "ok":
                    dialog.dispose();
                    dialog = null;
                    break;
                case "cancel":
                    dialog.dispose();
                    dialog = null;
                    break;
                case "return": 
                 //   ArrayList<Borrow> list = tableModel.getRows(dialog.getBookListTable().getSelectedRows());
                   // BorrowService.getInstance().returnBooks(list);
                    break;
                default:
                    break;
            }
        }
    }
}
