/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.ApplicationLog;
import io.Refresh;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {

    private MainView mainView;
    private MenuController menuController;
    
    /*
     * TABs controllers
     */
    
    private BookTabController bookTabController;
    private BorrowTabController borrowTabController;
    private NotificationTabController notificationTabController;
    private CustomerTabController customerTabController;

    public MainController() {
        // Hlavní pohled
        mainView = new MainView();

        // Ukončení aplikace
        mainView.addWindowListener(new MainViewWindowListener());

        // Menu controller
        menuController = new MenuController(mainView);

        // Ostatní controller(y)
        bookTabController = new BookTabController(mainView);
        borrowTabController = new BorrowTabController(mainView);
        notificationTabController = new NotificationTabController(mainView);
        customerTabController = new CustomerTabController(mainView);
        
        // Refresh module
        Refresh.getInstance().setControllers(bookTabController, customerTabController, borrowTabController, notificationTabController);
        
        // registrace listeneru logu
        ApplicationLog.getInstance().registerListener(this);

        // Zobrazeni prihlaseneho uzivatele
        mainView.getSystemUserLabel().setText(AppController.getInstance().getLoggedUser().toString());
        ApplicationLog.getInstance().addMessage("Úspěšné přihlášení uživatele (" + AppController.getInstance().getLoggedUser().toString() + ")");

    }

    @Override
    void showView() {
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }

    @Override
    void dispose() {
        ApplicationLog.getInstance().removeListener(this);
        mainView.dispose();
        mainView = null;
    }

    @Override
    public void logChanged() {
        mainView.getConsole().setText(ApplicationLog.getInstance().getLastLog() + "\n" + mainView.getConsole().getText());
    }

    private class MainViewWindowListener implements WindowListener {

        public MainViewWindowListener() {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            int exit = JOptionPane.showInternalConfirmDialog(mainView.getContentPane(), "Opravdu si přejete ukončit program?", "Ukončit?", JOptionPane.OK_CANCEL_OPTION);
            if (exit == JOptionPane.OK_OPTION) {
                System.exit(1);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }
}