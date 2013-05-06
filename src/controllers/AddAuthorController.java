/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.entity.Author;
import services.AuthorService;
import views.AddAuthorDialog;

/**
 *
 * @author Nesh
 */
public class AddAuthorController extends BaseController {

    private AddAuthorDialog dialog;
    private Author author;

    public AddAuthorController() {
        dialog = new AddAuthorDialog(null, true);
        initListeners();
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

    private void initListeners() {
        //ActionListener
        AddAuthorActionListener a = new AddAuthorActionListener();
        dialog.getSaveButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);

        //KeyListener
        AddAuthorKeyListener k = new AddAuthorKeyListener();
        dialog.getInputFName().addKeyListener(k);
        dialog.getInputLName().addKeyListener(k);
    }

    public Author getAuthor() {
        return author;
    }

    private class AddAuthorActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "saveButton":

                    String fName = dialog.getInputFName().getText().trim();
                    String lName = dialog.getInputLName().getText().trim();

                    // Alespon jedna informace musi byt zadana
                    if (fName.isEmpty() && lName.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Alespoň jeden údaj musí být zadaný", "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    author = new Author(fName, lName);
                    dialog.dispose();
                    break;
                case "cancelButton":
                    dispose();
                    break;
                default:
                    break;

            }
        }
    }

    private class AddAuthorKeyListener implements KeyListener {

        List<Author> authors;

        public AddAuthorKeyListener() {
            authors = AuthorService.getInstance().getAuthors();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // pokud se nezapise znak - hned skonci
            if (String.valueOf(e.getKeyChar()).trim().isEmpty()) {
                return;
            }

            // priprava promennych 
            String in;
            int start;

            // co se doplňuje 
            switch (((JComponent) e.getSource()).getName()) {

                // doplnění jména
                case "fname":
                    in = dialog.getInputFName().getText().trim();
                    start = in.length();

                    // zadany aspon 2 znaky
                    if (start > 1) {
                        for (Author a : authors) {
                            if (a.getFirstName() != null && a.getFirstName().startsWith(in)) {
                                dialog.getInputFName().setText(a.getFirstName());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputFName().setSelectionStart(start);
                    dialog.getInputFName().setSelectionEnd(dialog.getInputFName().getText().length());
                    break;

                // doplnění přijmení    
                case "lname":
                    in = dialog.getInputLName().getText().trim();
                    start = in.length();

                    // zadany aspon 3 znaky
                    if (start > 2) {
                        for (Author a : authors) {
                            if (a.getLastName() != null && a.getLastName().startsWith(in)) {
                                dialog.getInputLName().setText(a.getLastName());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputLName().setSelectionStart(start);
                    dialog.getInputLName().setSelectionEnd(dialog.getInputLName().getText().length());
                    break;
                default:
                    break;

            }
        }
    }
}
