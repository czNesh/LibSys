package controllers;

import helpers.Validator;
import io.ApplicationLog;
import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.SystemUser;
import services.SystemUserService;
import views.SystemUserDetailDialog;

/**
 * Třída (controller) starající se o pohled na uživatele
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class SystemUserDetailController extends BaseController {

    private SystemUserDetailDialog dialog; // připojený pohled
    private SystemUser user; // zobrazený uživatel
    private boolean editMode; // indikace editace záznamu

    /**
     * Třídní konstruktor nastaví uživatele
     *
     * @param user nastaví uživatele
     */
    public SystemUserDetailController(SystemUser user) {
        dialog = new SystemUserDetailDialog(null, true);
        this.user = user;
        updateData();
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        SystemUserDetailActionListener a = new SystemUserDetailActionListener();
        dialog.getBTNedit().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNrenew().addActionListener(a);
        dialog.getBTNdelete().addActionListener(a);
    }

    /**
     * Uloží změny v editovaném uživateli
     */
    private void saveSystemUser() {
        StringBuilder validationLog = new StringBuilder();

        String firstName = dialog.getINPfirstName().getText().trim();
        String lastName = dialog.getINPlastName().getText().trim();
        String email = dialog.getINPemail().getText().trim();
        String login = dialog.getINPlogin().getText().trim();
        String password = dialog.getINPpassword().getText().toString().trim();
        boolean master = dialog.getINPmaster().isSelected();

        if (!Validator.isValidString(firstName)) {
            validationLog.append("Vyplňte jméno\n");
        }

        if (!Validator.isValidString(lastName)) {
            validationLog.append("Vyplňte přijmení\n");
        }

        if (!Validator.isValidString(login)) {
            validationLog.append("Vyplňte login\n");
        }


        if (!Validator.isValidEmail(email)) {
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
        updateData();
    }

    /**
     * Přepnutí pohledu z prohlížení do editace a zpět
     */
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

    /**
     * Update dat v pohledu
     */
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

    /**
     * Smaže uživatele
     */
    private void deleteSystemUser() {
        user.setDeleted(true);
        SystemUserService.getInstance().save(user);
        ApplicationLog.getInstance().addMessage("Uživatel " + user.getFullName() + " úspěšně odebrán");
        updateData();
    }

    /**
     * Obnoví smazaného uživatele
     */
    private void renewSystemUser() {
        user.setDeleted(false);
        SystemUserService.getInstance().save(user);
        ApplicationLog.getInstance().addMessage("Uživatel " + user.getFullName() + " úspěšně obnoven");
        updateData();
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class SystemUserDetailActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "delete":
                    deleteSystemUser();
                    break;
                case "renew":
                    renewSystemUser();
                    break;
                case "edit":
                    switchMode();
                    break;
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
