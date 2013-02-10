/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MenuController implements ActionListener {

    private MainView view;
    private MainController controller;

    public MenuController(MainController controller, MainView view) {
        this.controller = controller;
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getNewItemButton().addActionListener(this);
        view.getNewCustomerMenuItem().addActionListener(this);
        view.getCustomerListMenuItem().addActionListener(this);
        view.getNewBorrowButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JComponent b = (JComponent) e.getSource();
        String name = b.getName();

        switch (name) {
            case "newItem":
                NewItemController newItemController = new NewItemController(view, controller);
                newItemController.showView();
                break;
             case "newBorrow":
                BookBorrowController bookBorrowController = new BookBorrowController(view);
                bookBorrowController.showView();
                break;                              
            case "newCustomer":
                NewCustomerController newCustomerController = new NewCustomerController(view);
                newCustomerController.showView();
                break;
            case "browseCustomers":
                CustomerListController customerListController = new CustomerListController(view);
                customerListController.showView();
                break;
            case "importItems":
                break;
            case "exportItems":
                break;
            case "barcode":
                break;
            case "qrcode":
                break;
            default:
                System.out.println("Žádná akce");
                break;
        }
    }
}
