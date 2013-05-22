package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import models.entity.SystemUser;
import services.SystemUserService;
import views.LoginView;

/**
 * Třída (controller) přihlašovacího pohledu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class LoginController extends BaseController {

    private LoginView loginView; // přihlašovací pohled

    /**
     * konstruktor třídy
     */
    public LoginController() {
        loginView = new LoginView();
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    public void showView() {
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        loginView.dispose();
        loginView = null;
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        LoginActionListener a = new LoginActionListener();
        loginView.getBTNlogin().addActionListener(a);
        loginView.getBTNreset().addActionListener(a);
    }

    /**
     * Metoda pro přihlášení uživatele
     */
    private void login() {
        // Získání vstupních hodnot
        String login = loginView.getINPlogin().getText();
        String password = String.valueOf(loginView.getINPpassword().getPassword());


        // VALIDACE
        if (login.isEmpty()) {
            loginView.getInfoLabel().setText("Prázdné uživatelské jméno");
            return;
        }

        if (password.isEmpty()) {
            loginView.getInfoLabel().setText("Prázdné heslo");
            return;
        }

        // Zpracování
        SystemUser temp = SystemUserService.getInstance().login(login, password);

        if (temp != null) {
            AppController.getInstance().setLoggedUser(temp);
            AppController.getInstance().showMainFrame();
        } else {
            loginView.getInfoLabel().setText("Neplatná kombinace jména a hesla");
        }
    }

    /**
     * Smazání vstupních polí pohledu
     */
    private void reset() {
        // Smazání polí
        loginView.getINPlogin().setText("");
        loginView.getINPpassword().setText("");
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class LoginActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "login":
                    login();
                    break;
                case "reset":
                    reset();
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }
}
