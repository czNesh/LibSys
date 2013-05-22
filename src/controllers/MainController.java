package controllers;

import io.ApplicationLog;
import io.Configuration;
import io.ReaderPostProcessor;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import views.MainView;

/**
 * Třída (controller) hlavního pohledu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class MainController extends BaseController {

    private MainView mainView; // hlavní pohled
    private MenuController menuController; // menu controller 
    /*
     * TABs controllers
     */
    private BookTabController bookTabController;
    private BorrowTabController borrowTabController;
    private NotificationTabController notificationTabController;
    private CustomerTabController customerTabController;

    /**
     * Konstruktor třídy
     */
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

        // RefreshController module
        RefreshController.getInstance().setControllers(bookTabController, customerTabController, borrowTabController, notificationTabController);

        // registrace listeneru logu
        ApplicationLog.getInstance().registerListener(this);

        // Zobrazeni prihlaseneho uzivatele
        mainView.getSystemUserLabel().setText(AppController.getInstance().getLoggedUser().toString());
        ApplicationLog.getInstance().addMessage("Úspěšné přihlášení uživatele (" + AppController.getInstance().getLoggedUser().toString() + ")");

        // Kontrola vypnutého přihlašování - potlačení tlačítka pro odhlášení
        if (Configuration.getInstance().isSkipLogging()) {
            mainView.getLogoutMenuItem().setVisible(false);
        }

        // LibSysServer Autostart
        if (Configuration.getInstance().isServerAutoStart()) {
            SocketServerController.getInstance().setServerStatus(true);
        }

        // Reader listener
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(new ReaderPostProcessor());
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        ApplicationLog.getInstance().removeListener(this);
        mainView.dispose();
        mainView = null;
    }

    /**
     * Detekce nových zpráv do logu
     */
    @Override
    public void logChanged() {
        if (mainView.getConsole().getText().isEmpty()) {
            mainView.getConsole().setText(ApplicationLog.getInstance().getLastLog());
        } else {
            mainView.getConsole().setText(mainView.getConsole().getText() + "\n" + ApplicationLog.getInstance().getLastLog());
        }

    }

    /**
     * Třida starající se o vypnutí (nemá zde zatím jíný účel)
     */
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