package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import views.HelpDialog;
import views.MainView;

/**
 * Třída (controller) starající se o hlavní menu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class MenuController implements ActionListener {

    private MainView view; // hlavní pohled

    /**
     * Konstuktor třídy nastaví hlavní pohled
     *
     * @param view hlavní pohled
     */
    public MenuController(MainView view) {
        this.view = view;
        initListeners();
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        view.getNewItemButton().addActionListener(this);
        view.getNewBorrowButton().addActionListener(this);
        view.getLogoutMenuItem().addActionListener(this);
        view.getExitMenuItem().addActionListener(this);
        view.getNewCustomerButton().addActionListener(this);
        view.getConnectionButton().addActionListener(this);
        view.getSettingsMenuItem().addActionListener(this);
        view.getHelpMenuItem().addActionListener(this);
    }

    /**
     * Detekce akce
     *
     * @param e událost
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JComponent) e.getSource()).getName()) {
            case "newItem":
                newItem();
                break;
            case "newBorrow":
                newBorrow();
                break;
            case "newCustomerButton":
                newCustomer();
                break;
            case "connection":
                serverLibSys();
                break;
            case "settings":
                settingsDialog();
                break;
            case "logout":
                logout();
                break;
            case "exit":
                exit();
                break;
            case "help":
                help();
            default:
                // DO NOTHING
                break;
        }
    }

    /**
     * Nová kniha
     */
    private void newItem() {
        NewBookController newItemController = new NewBookController(view);
        newItemController.showView();
        RefreshController.getInstance().refreshBookTab();
    }

    /**
     * Nová půjčka
     */
    private void newBorrow() {
        NewBorrowController bookBorrowController = new NewBorrowController(view);
        bookBorrowController.showView();
    }

    /**
     * Nový zákazník
     */
    private void newCustomer() {
        NewCustomerController newCustomerController2 = new NewCustomerController(view);
        newCustomerController2.showView();
    }

    /**
     * Nastavení programu
     */
    private void settingsDialog() {
        SettingsController sc = new SettingsController();
        sc.showView();
        view.getLogoutMenuItem().setVisible(!Configuration.getInstance().isSkipLogging());
    }

    /**
     * LibSys Server
     */
    private void serverLibSys() {
        SocketServerController.getInstance().showView();
    }

    /**
     * Odhlášení
     */
    private void logout() {
        int logout = JOptionPane.showInternalConfirmDialog(view.getContentPane(), "Opravdu se chcete odhlásit?", "Odhlásit?", JOptionPane.OK_CANCEL_OPTION);
        if (logout == JOptionPane.OK_OPTION) {
            SocketServerController.getInstance().setServerStatus(false);
            AppController.getInstance().setLoggedUser(null);
            AppController.getInstance().showLoginFrame();
        }
    }

    /**
     * Vypnutí aplikace
     */
    private void exit() {
        int exit = JOptionPane.showInternalConfirmDialog(view.getContentPane(), "Opravdu si přejete ukončit program?", "Ukončit?", JOptionPane.OK_CANCEL_OPTION);
        if (exit == JOptionPane.OK_OPTION) {
            System.exit(1);
        }
    }

    /**
     * Nápověda
     */
    private void help() {
        HelpDialog dialog = new HelpDialog(view, false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
