/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.BookTableModel;
import models.entity.Borrow;
import services.BookService;
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
        setValues();
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

    private void setValues() {
        dialog.getCustomerSSNTextField().setText(String.valueOf(item.getCustomer().getSSN()));
        dialog.getCustomerNameTextField().setText(item.getCustomer().getFullName());
        dialog.getCustomerAdressTextField().setText(item.getCustomer().getFullAdress());
        dialog.getCustomerPhoneTextField().setText(item.getCustomer().getPhone());
        dialog.getCustomerEmailTextField().setText(item.getCustomer().getEmail());
        dialog.getCustomerNotesTextArea().setText(item.getCustomer().getNotes());
        
        dialog.getBookListTable().setModel(tableModel);
    }
}
