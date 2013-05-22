package controllers;

import helpers.Validator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.Author;
import views.AddAuthorDialog;

/**
 * Třída (controller) pro přídání nového autora
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class AddAuthorController extends BaseController {

    private AddAuthorDialog dialog; // připojený pohled
    private Author author; // vytvářený autor

    /**
     * Konstruktor třídy
     */
    public AddAuthorController() {
        dialog = new AddAuthorDialog(null, true);
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
        //ActionListener
        AddAuthorActionListener a = new AddAuthorActionListener();
        dialog.getSaveButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);
    }

    /**
     * Vrací vyplněného autora
     * @return vyplněný autor
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Zkontroluje a případně uloží autora do proměnné
     */
    private void saveAuthor() {
        // Získání dat z pohledu
        String fName = dialog.getInputFName().getText().trim();
        String lName = dialog.getInputLName().getText().trim();

        // VALIDACE (jeden ze dvou)
        if (Validator.isOneOfTheTwoFilled(fName, lName)) {
            JOptionPane.showMessageDialog(dialog, "Alespoň jeden údaj musí být zadaný", "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Uložení a ukončení
        author = new Author(fName, lName);
        dialog.dispose();
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class AddAuthorActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "saveButton": 
                    saveAuthor();
                    break;
                case "cancelButton":
                    dispose(); 
                    break;
                default:
                    break;

            }
        }
    }
}
