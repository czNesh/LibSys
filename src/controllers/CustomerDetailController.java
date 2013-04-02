/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.entity.Customer;
import views.CustomerDetailDialog;

/**
 *
 * @author petr.hejhal
 */
public class CustomerDetailController extends BaseController {

    private CustomerDetailDialog dialog;
    Customer customer;

    public CustomerDetailController(Customer customer) {
        this.customer = customer;

        dialog = new CustomerDetailDialog(null, true);
    }

    @Override
    void showView() {
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }
}
