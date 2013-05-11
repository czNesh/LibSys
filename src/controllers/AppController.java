/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.entity.SystemUser;

/**
 *
 * @author Nesh
 */
public class AppController {

    private static AppController instance;
    private BaseController actual;
    private SystemUser loggedUser;

    public static AppController getInstance() {
        synchronized (AppController.class) {
            if (instance == null) {
                instance = new AppController();
            }
        }
        return instance;
    }

    private AppController() {
        actual = new LoginController();
    }

    public void go() {
        actual.showView();
    }

    void showMainFrame() {
        actual.dispose();
        actual = new MainController();
        actual.showView();
    }

    void showLoginFrame() {
        actual.dispose();
        actual = new LoginController();
        actual.showView();
    }

    /**
     * @return the loggedUser
     */
    public SystemUser getLoggedUser() {
        return loggedUser;
    }

    /**
     * @param loggedUser the loggedUser to set
     */
    public void setLoggedUser(SystemUser loggedUser) {
        this.loggedUser = loggedUser;
    }
}
