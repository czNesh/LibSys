package controllers;

import models.entity.SystemUser;

/**
 * Třída (controller) řídící spouštění aplikace
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class AppController {

    private static AppController instance; // instance této třídy
    private BaseController actual; // aktivní controller: hlavní / Login
    private SystemUser loggedUser; // přihlášený uživatel

    /**
     * SINGLETON - AppController
     *
     * @return instace tídy AppController
     */
    public static AppController getInstance() {
        synchronized (AppController.class) {
            if (instance == null) {
                instance = new AppController();
            }
        }
        return instance;
    }

    /**
     * Defaultní konstruktor
     */
    private AppController() {
        actual = new LoginController();
    }

    /**
     * Start pohledu
     */
    public void go() {
        actual.showView();
    }

    /**
     * Zobrazí hlavní pohled aplikace
     */
    void showMainFrame() {
        actual.dispose();
        actual = new MainController();
        actual.showView();
    }

    /**
     * Zobrazí přihlašovací pohled
     */
    void showLoginFrame() {
        actual.dispose();
        actual = new LoginController();
        actual.showView();
    }

    /**
     * Vrací aktuálně přihlášeného uživatele
     *
     * @return přihlášený uživatel
     */
    public SystemUser getLoggedUser() {
        return loggedUser;
    }

    /**
     * @param loggedUser nastaví přihlášeného uživatele
     */
    public void setLoggedUser(SystemUser loggedUser) {
        this.loggedUser = loggedUser;
    }
}
