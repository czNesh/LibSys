/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.entity.Borrow;
import views.BorrowDetailDialog;

/**
 *
 * @author Nesh
 */
public class BorrowDetailController extends BaseController {

    private BorrowDetailDialog dialog;
    private Borrow item;

    public BorrowDetailController(Borrow item) {
        this.item = item;
        dialog = new BorrowDetailDialog(null, true);
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
    }
}
