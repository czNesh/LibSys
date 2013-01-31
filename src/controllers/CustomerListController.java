/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.swing.JFrame;
import views.CustomerListDialog;

/**
 *
 * @author Nesh
 */
public class CustomerListController extends BaseController{
    
    CustomerListDialog dialog;

    public CustomerListController(JFrame parent) {
        dialog = new CustomerListDialog(parent, true);     
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
    
}
