/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MenuController implements ActionListener {

    private MainView view;

    public MenuController(MainView view) {
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getNewItemButton().addActionListener(this);
        view.getNewCustomerMenuItem().addActionListener(this);
        view.getCustomerListMenuItem().addActionListener(this);
        view.getNewBorrowButton().addActionListener(this);
        view.getLogoutMenuItem().addActionListener(this);
        view.getExitMenuItem().addActionListener(this);
        view.getNewCustomerButton().addActionListener(this);
        view.getQRCodeButton().addActionListener(this);
        view.getBarcodeButton().addActionListener(this);
        view.getConnectionButton().addActionListener(this);
        view.getSettingsMenuItem().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JComponent) e.getSource()).getName()) {
            case "newItem":
                NewBookController newItemController = new NewBookController(view);
                newItemController.showView();
                RefreshController.getInstance().refreshBookTab();
                break;
            case "newBorrow":
                BookBorrowController bookBorrowController = new BookBorrowController(view);
                bookBorrowController.showView();
                break;
            case "newCustomer":
                NewCustomerController newCustomerController = new NewCustomerController(view);
                newCustomerController.showView();

                break;
            case "newCustomerButton":
                NewCustomerController newCustomerController2 = new NewCustomerController(view);
                newCustomerController2.showView();
                break;
            case "browseCustomers":
                CustomerListController customerListController = new CustomerListController(view, false);
                customerListController.showView();
                break;
            case "importItems":
                break;
            case "exportItems":
                break;
            case "connection":
                SocketServerController.getInstance().showView();
                break;
            case "settings":
                SettingsController sc = new SettingsController();
                sc.showView();
                break;
            case "logout":
                int logout = JOptionPane.showInternalConfirmDialog(view.getContentPane(), "Opravdu se chcete odhlásit?", "Odhlásit?", JOptionPane.OK_CANCEL_OPTION);
                if (logout == JOptionPane.OK_OPTION) {
                    AppController.getInstance().setLoggedUser(null);
                    AppController.getInstance().showLoginFrame();
                }
                break;
            case "exit":
                int exit = JOptionPane.showInternalConfirmDialog(view.getContentPane(), "Opravdu si přejete ukončit program?", "Ukončit?", JOptionPane.OK_CANCEL_OPTION);
                if (exit == JOptionPane.OK_OPTION) {
                    System.exit(1);
                }
                break;
            default:
                System.out.println("Žádná akce");
                break;
        }
    }
}
