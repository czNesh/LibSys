/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.SystemUser;
import services.SystemUserService;
import views.NewSystemUserDialog;

/**
 *
 * @author Nesh
 */
public class NewSystemUserController extends BaseController {

    private NewSystemUserDialog dialog;
    private boolean success = false;

    public NewSystemUserController() {
        dialog = new NewSystemUserDialog(null, true);

        initListeners();
    }

    private void initListeners() {
        NewSystemUserActionListener a = new NewSystemUserActionListener();
        dialog.getBTNsave().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
    }

    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    private void saveSystemUser() {
        StringBuilder validationLog = new StringBuilder();

        String firstName = dialog.getINPfirstName().getText().trim();
        String lastName = dialog.getINPlastName().getText().trim();
        String email = dialog.getINPemail().getText().trim();
        String login = dialog.getINPlogin().getText().trim();
        String password = dialog.getINPpassword().getText().trim();
        boolean master = dialog.getINPmaster().isSelected();

        SystemUser u = new SystemUser();

        if (firstName == null || firstName.isEmpty()) {
            validationLog.append("Vyplňte jméno\n");
        } else {
            u.setFirstName(firstName);
        }

        if (lastName == null || lastName.isEmpty()) {
            validationLog.append("Vyplňte přijmení\n");
        } else {
            u.setLastName(lastName);
        }

        if (login == null || login.isEmpty()) {
            validationLog.append("Vyplňte login\n");
        } else {
            u.setLogin(login);
        }

        if (password == null || password.isEmpty()) {
            validationLog.append("Vyplňte heslo\n");
        } else {
            u.setPassword(password);
        }
        if (email == null || !email.matches(".+@.+\\.[a-z]+")) {
            validationLog.append("Neplatný email\n");
        } else {
            u.setEmail(email);
        }

        u.setMaster(master);

        if (validationLog.length() > 0) {
            JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!SystemUserService.getInstance().isSavePossible(login)) {
                JOptionPane.showMessageDialog(dialog, "Login již využívá někdo jiný", "změňte login", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SystemUserService.getInstance().save(u);
            success = true;
            dispose();
        }

    }

    public boolean isSuccess() {
        return success;
    }

    private class NewSystemUserActionListener implements ActionListener {

        public NewSystemUserActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "save":
                    saveSystemUser();
                    break;
                case "cancel":
                    dispose();
                    break;
            }
        }
    }
}
