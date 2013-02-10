/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.SystemUserService;
import models.entity.SystemUser;
import views.LoginView;

/**
 *
 * @author Nesh
 */
public class LoginController extends BaseController {

    private LoginView loginView;

    public LoginController() {
        loginView = new LoginView(null, true);
        initListeners();
    }

    private void initListeners() {
        loginView.getLoginButton().addActionListener(new LoginButtonClickedListener());
        loginView.getResetButton().addActionListener(new ResetButtonClickedListener());
    }

    @Override
    public void showView() {
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }

    @Override
    void dispose() {
        loginView.dispose();
        loginView = null;
    }

    private class LoginButtonClickedListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Získání vstupních hodnot
            String login = loginView.getLoginInput().getText();
            String password = String.valueOf(loginView.getPasswordInput().getPassword());


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

            if(temp != null){
                AppController.getInstance().setLoggedUser(temp);
                AppController.getInstance().showMainFrame();
            }else{
                loginView.getInfoLabel().setText("Neplatná kombinace jména a hesla");
            }
                
        }
    }

    private class ResetButtonClickedListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Log / Info
            System.out.println("ACTION DETECTED: Reset action"); // TODO (log & info sout)            

            // Smazání polí
            loginView.getLoginInput().setText("");
            loginView.getPasswordInput().setText("");
        }
    }
}
