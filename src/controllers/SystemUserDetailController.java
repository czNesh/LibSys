/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.ApplicationLog;
import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.SystemUser;
import org.omg.CORBA.portable.ApplicationException;
import services.SystemUserService;
import views.SystemUserDetailDialog;

/**
 *
 * @author Nesh
 */
public class SystemUserDetailController extends BaseController {

    private SystemUserDetailDialog dialog;
    private SystemUser user;
    private boolean editMode;

    public SystemUserDetailController(SystemUser user) {
        dialog = new SystemUserDetailDialog(null, true);
        this.user = user;
        updateData();
        initListeners();
    }

    private void initListeners() {
        SystemUserDetailActionListener a = new SystemUserDetailActionListener();
        dialog.getBTNedit().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNrenew().addActionListener(a);
        dialog.getBTNdelete().addActionListener(a);
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

        if (firstName == null || firstName.isEmpty()) {
            validationLog.append("Vyplňte jméno\n");
        }

        if (lastName == null || lastName.isEmpty()) {
            validationLog.append("Vyplňte přijmení\n");
        }

        if (login == null || login.isEmpty()) {
            validationLog.append("Vyplňte login\n");
        }


        if (email == null || !email.matches(".+@.+\\.[a-z]+")) {
            validationLog.append("Neplatný email\n");
        }

        if (validationLog.length() > 0) {
            JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!password.isEmpty()) {
                int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Dojde ke změně hesla", "Opravdu pokračovat?", JOptionPane.OK_CANCEL_OPTION);
                if (isSure != JOptionPane.OK_OPTION) {
                    dialog.getINPpassword().setText("");
                    return;
                }
                user.setPassword(SystemUserService.getInstance().getHash(password));
            }
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLogin(login);
            user.setMaster(master);
            if (user.getLogin().equals("default")) {
                Configuration.getInstance().setDefaultEmail(email);
            }
            SystemUserService.getInstance().save(user);
            ApplicationLog.getInstance().addMessage("Změna uživatele " + user.getFullName() + " úspěšně uložena");
            switchMode();
        }

    }

    private void switchMode() {
        if (editMode) {
            dialog.getBTNedit().setName("edit");
            dialog.getBTNedit().setText("Upravit");
        } else {
            dialog.getBTNedit().setName("save");
            dialog.getBTNedit().setText("Uložit");
        }

        dialog.getINPfirstName().setEditable(!editMode);
        dialog.getINPlastName().setEditable(!editMode);
        dialog.getINPemail().setEditable(!editMode);
        dialog.getINPmaster().setEnabled(!editMode);
        dialog.getINPlogin().setEditable(!editMode);
        dialog.getINPpassword().setEditable(!editMode);


        editMode = !editMode;

        if (user.getLogin().equals("default")) {
            dialog.getINPfirstName().setEditable(false);
            dialog.getINPlastName().setEditable(false);
            dialog.getINPmaster().setEnabled(false);
            dialog.getINPlogin().setEditable(false);
        }

        dialog.revalidate();
        dialog.repaint();


    }

    private void updateData() {
        dialog.getINPfirstName().setText(user.getFirstName());
        dialog.getINPlastName().setText(user.getLastName());
        dialog.getINPemail().setText(user.getEmail());
        dialog.getINPmaster().setSelected(user.isMaster());
        dialog.getINPlogin().setText(user.getLogin());

        dialog.getBTNdelete().setVisible(!user.isDeleted());
        dialog.getBTNrenew().setVisible(user.isDeleted());

        if (user.getLogin().equals("default")) {
            dialog.getBTNdelete().setVisible(false);
        }
    }

    private class SystemUserDetailActionListener implements ActionListener {

        public SystemUserDetailActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "delete":
                    user.setDeleted(true);
                    SystemUserService.getInstance().save(user);
                    ApplicationLog.getInstance().addMessage("Uživatel " + user.getFullName() + " úspěšně odebrán");
                    updateData();
                    break;
                case "renew":
                    user.setDeleted(false);
                    SystemUserService.getInstance().save(user);
                    ApplicationLog.getInstance().addMessage("Uživatel " + user.getFullName() + " úspěšně obnoven");
                    updateData();
                    break;
                case "edit":
                    switchMode();
                    break;
                case "save":
                    saveSystemUser();
                    updateData();
                    break;
                case "cancel":
                    dispose();
                    break;
            }
        }
    }
}
