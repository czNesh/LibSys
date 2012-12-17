/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Nesh
 */
public class AppController {

    private static AppController instance;
    private BaseController actual;

    public static AppController getInstance() {
        synchronized (AppController.class) {
            if (instance == null) {
                instance = new AppController();
            }
        }
        return instance;
    }

    public AppController() {
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
}
