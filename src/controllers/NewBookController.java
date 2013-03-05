/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import models.BookTableModel;
import models.entity.Author;
import models.entity.Book;
import remote.GoogleService;
import services.BookService;
import views.DatePicker;
import views.NewBookDialog;
import views.SearchResultsDialog;

/**
 *
 * @author Administrator
 */
public class NewBookController extends BaseController {

    private NewBookDialog dialog;
    private SearchResultsDialog resOut;
    private GoogleService gbs;
    private Book selectedBoook;
    private ArrayList<Book> foundedItems;
    DefaultListModel<Author> authorListModel = new DefaultListModel();

    public NewBookController(JFrame parent) {
        gbs = new GoogleService();
        dialog = new NewBookDialog(parent, false);
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
        // ActionListener
        NewBookActionListener a = new NewBookActionListener();
        dialog.getSaveButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);
        dialog.getAddAuthorButton().addActionListener(a);
        dialog.getRemoveAuthorButton().addActionListener(a);
        dialog.getDatePickerButton().addActionListener(a);
        dialog.getSearchButton().addActionListener(new SearchButtonListener());
    }

    private class NewBookActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {

                case "saveButton":

                    Book b = new Book();
                    StringBuilder errorOutput = new StringBuilder();

                    // TITUL
                    if (dialog.getInputTitle().getText() == null || dialog.getInputTitle().getText().trim().isEmpty()) {
                        errorOutput.append("- Vyplňte název knihy\n");
                    } else {
                        b.setTitle(dialog.getInputTitle().getText());
                    }

                    // AUTORI
                    if (authorListModel.isEmpty()) {
                        errorOutput.append("- Vyplňte autora / autory \n");
                    } else {
                        ArrayList<Author> temp = new ArrayList<>();
                        for (int i = 0; i < authorListModel.getSize(); i++) {
                            temp.add(authorListModel.get(i));
                        }
                        b.setAuthors(temp);
                        b.setMainAuthor(temp.get(0));
                    }

                    //vydavatel 
                    b.setPublisher(dialog.getInputPublisher().getText());
                    //jazyk
                    b.setLanguage(dialog.getInputLanguage().getText());

                    //rok vydání
                    b.setPublishedYear((Date) dialog.getInputPublishedDate().getValue());


                    // ISBN 10
                    if (dialog.getInputISBN10().getText() == null || dialog.getInputISBN10().getText().trim().isEmpty()) {
                        errorOutput.append("- Vyplňte ISBN 10\n");
                    } else {
                        b.setISBN10(dialog.getInputISBN10().getText());
                    }

                    // ISBN 13
                    if (dialog.getInputISBN13().getText() == null || dialog.getInputISBN13().getText().trim().isEmpty()) {
                        errorOutput.append("- vyplňte ISBN 13\n");
                    } else {
                        b.setISBN13(dialog.getInputISBN13().getText());
                    }

                    // datum pridani

                    b.setAddedDate(DateFormater.stringToDate(dialog.getInputBuyedDate().getText(), false));

                    // Umístění 
                    if (dialog.getInputLocation().getText() == null || dialog.getInputLocation().getText().trim().isEmpty()) {
                        errorOutput.append("Vyplňte prosím umístění\n");
                    } else {
                        b.setLocation(dialog.getInputLocation().getText());
                    }

                    //pages 
                    b.setPageCount((int) dialog.getInputPageCount().getValue());

                    // Check validation results
                    if (errorOutput.length() == 0) {
                        BookService.getInstance().saveBook(b,(int) dialog.getInputCount().getValue());
                        // UPDATE TABULKY
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, errorOutput.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
                    }

                    break;
                case "cancelButton":
                    dispose();
                    break;
                case "addAuthorButton":

                    AddAuthorController aac = new AddAuthorController();
                    aac.showView();
                    if (aac.getAuthor() != null) {
                        authorListModel.add(authorListModel.getSize(), aac.getAuthor());
                        dialog.getInputAuthors().setModel(authorListModel);
                    }
                    break;
                case "removeAuthorButton":
                    int[] selectedIdx;
                    selectedIdx = dialog.getInputAuthors().getSelectedIndices();
                    for (int i = selectedIdx.length - 1; i >= 0; i--) {
                        authorListModel.remove(selectedIdx[i]);
                    }
                    dialog.getInputAuthors().setModel(authorListModel);
                    break;
                case "datePickerButton":

                    DatePicker dp = new DatePicker(null, true);
                    dp.setLocationRelativeTo(null);
                    dp.setVisible(true);

                    if (dp.getDate() != null) {
                        Date d = dp.getDate();
                        dialog.getInputBuyedDate().setText(DateFormater.dateToString(d, false));
                    }

                    break;
                default:
                    break;

            }
        }
    }

    private class TableClickedMouseListener implements MouseListener {

        public TableClickedMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();
                JTable t = resOut.getResultsTable();
                int index = t.rowAtPoint(p);

                selectedBoook = foundedItems.get(index);

                if (selectedBoook.getTitle() != null) {
                    dialog.getInputTitle().setText(selectedBoook.getTitle());
                }
                if (!selectedBoook.getAuthors().isEmpty()) {
                    authorListModel.removeAllElements();
                    for (Author a : selectedBoook.getAuthors()) {
                        authorListModel.add(authorListModel.getSize(), a);
                    }
                    dialog.getInputAuthors().setModel(authorListModel);
                }
                if (selectedBoook.getISBN10() != null) {
                    dialog.getInputISBN10().setText(selectedBoook.getISBN10());
                }
                if (selectedBoook.getISBN13() != null) {
                    dialog.getInputISBN13().setText(selectedBoook.getISBN13());
                }
                if (selectedBoook.getPageCount() != 0) {
                    dialog.getInputPageCount().setValue(selectedBoook.getPageCount());
                }
                if (selectedBoook.getLanguage() != null) {
                    dialog.getInputLanguage().setText(selectedBoook.getLanguage());
                }
                if (selectedBoook.getPublisher() != null) {
                    dialog.getInputPublisher().setText(selectedBoook.getPublisher());
                }
                if (selectedBoook.getPublishedYear() != null) {
                    dialog.getInputPublishedDate().setValue(selectedBoook.getPublishedYear());
                }
                resOut.dispose();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private class SearchButtonListener implements ActionListener {

        public SearchButtonListener() {
            gbs = new GoogleService();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Zamezí opětovnému spuštění
            dialog.getSearchButton().setEnabled(false);

            // Nastavý hodnoty pro vyhledávání
            for (int i = 0; i < authorListModel.getSize(); i++) {
                gbs.setAutor(authorListModel.get(i).toString());
            }
            gbs.setTitle(dialog.getInputTitle().getText());
            gbs.setISBN(dialog.getInputISBN13().getText());
            gbs.setISBN(dialog.getInputISBN10().getText());

            // Vyhledávání
            gbs.run();

            // LOG


            if (gbs.getResultsCount() > 0) {

                // Pripravi dialog pro zobrazení výsledku
                // Nastaví listener
                resOut = new SearchResultsDialog(null, true);
                resOut.getResultsTable().addMouseListener(new TableClickedMouseListener());

                // Ulozi nalezene vysledky do pole 
                foundedItems = new ArrayList<>();
                foundedItems.addAll(gbs.getResults());

                // Pripravi table model a zobrazi vysledky
                BookTableModel ctm = new BookTableModel(foundedItems);
                resOut.getResultsTable().setModel(ctm);
                resOut.setLocationRelativeTo(null);
                resOut.setVisible(true);

            }

            // Znovu zpristupni tlacitko pro hledaní
            dialog.getSearchButton().setEnabled(true);
            dialog.getSearchButton().setText("Hledat znovu");
        }
    }
}
