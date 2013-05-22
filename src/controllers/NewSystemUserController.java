package controllers;

import helpers.Validator;
import io.ApplicationLog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.SystemUser;
import services.SystemUserService;
import views.NewSystemUserDialog;

/**
 * Třída (controller) starající se o přidání nového uživatele
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class NewSystemUserController extends BaseController {

    private NewSystemUserDialog dialog; // připojený pohled
    private boolean success = false; // indikace úspěšného přiidání

    /**
     * Třídní konstruktor
     */
    public NewSystemUserController() {
        dialog = new NewSystemUserDialog(null, true);

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
        NewSystemUserActionListener a = new NewSystemUserActionListener();
        dialog.getBTNsave().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
    }

    /**
     * Uložení nového uživatele
     */
    private void saveSystemUser() {
        StringBuilder validationLog = new StringBuilder();

        String firstName = dialog.getINPfirstName().getText().trim();
        String lastName = dialog.getINPlastName().getText().trim();
        String email = dialog.getINPemail().getText().trim();
        String login = dialog.getINPlogin().getText().trim();
        String password = dialog.getINPpassword().getText().trim();
        boolean master = dialog.getINPmaster().isSelected();

        SystemUser u = new SystemUser();

        if (Validator.isValidString(firstName)) {
            u.setFirstName(firstName);
        } else {
            validationLog.append("Vyplňte jméno\n");
        }

        if (Validator.isValidString(lastName)) {
            u.setLastName(lastName);
        } else {
            validationLog.append("Vyplňte přijmení\n");
        }

        if (Validator.isValidString(login)) {
            u.setLogin(login);
        } else {
            validationLog.append("Vyplňte login\n");
        }

        if (Validator.isValidString(password)) {
            u.setPassword(SystemUserService.getInstance().getHash(password));
        } else {
            validationLog.append("Vyplňte heslo\n");
        }
        if (Validator.isValidEmail(email)) {
            u.setEmail(email);
        } else {
            validationLog.append("Neplatný email\n");
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
            ApplicationLog.getInstance().addMessage("Vytvoření uživatele " + u.getFullName() + " proběhlo úspěšně");
            success = true;
            dispose();
        }

    }

    /**
     * Vrací zda bylo uložení úspěné
     *
     * @return indikace úspěšného uložení
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
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
