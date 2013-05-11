/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateHelper;
import io.Configuration;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import models.BookTableModel;
import models.entity.Author;
import models.entity.Book;
import models.entity.Genre;
import remote.GoogleSearch;
import services.BookService;
import services.GenreService;
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
    private GoogleSearch gbs;
    private Book selectedBoook;
    private ArrayList<Book> foundedItems;
    DefaultListModel<Author> authorListModel = new DefaultListModel();

    public NewBookController(JFrame parent) {
        gbs = new GoogleSearch(this);
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

        NewBookKeyListener k = new NewBookKeyListener();
        dialog.getInputAuthors().addKeyListener(k);
        dialog.getInputGenres().addKeyListener(k);
    }

    public void showResults() {
        // Znovu zpristupni tlacitko pro hledaní
        dialog.getSearchButton().setEnabled(true);
        dialog.getSearchButton().setText("Hledat znovu");
        dialog.getSearchButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));
       
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


        gbs = new GoogleSearch(this);
    }

    public void searchFail() {
        dialog.getSearchButton().setEnabled(true);
        dialog.getSearchButton().setText("Chyba: Opakovat?");
        dialog.getSearchButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));
    }

    private class NewBookKeyListener implements KeyListener {

        List<Genre> genres;

        public NewBookKeyListener() {
            genres = GenreService.getInstance().getList();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "authors":
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        authorListModel.remove(dialog.getInputAuthors().getSelectedIndex());
                        dialog.getInputAuthors().setModel(authorListModel);
                    }
                    break;
                case "genres":
                    //potlaceni mezer
                    if (dialog.getInputGenres().getText().endsWith(" ")) {
                        dialog.getInputGenres().setText(dialog.getInputGenres().getText().trim());
                        return;
                    }

                    // pokud se nezapise znak - hned skonci
                    if (String.valueOf(e.getKeyChar()).trim().isEmpty()) {
                        return;
                    }

                    if (e.getKeyChar() == ';') {
                        return;
                    }

                    // priprava promennych 
                    String in = dialog.getInputGenres().getText().trim();

                    String before = "";

                    if (in.contains(";")) {
                        int lastNewWord = in.lastIndexOf(';');
                        before = in.substring(0, lastNewWord + 1);
                        in = in.substring(lastNewWord + 1);
                    }

                    System.out.println("BEFORE " + before);
                    System.out.println("in " + in);
                    int start = in.length();

                    // zadany aspon 2 znaky
                    if (start > 1) {
                        for (Genre g : genres) {
                            if (g.getGenre() != null && g.getGenre().toLowerCase().startsWith(in.toLowerCase())) {
                                dialog.getInputGenres().setText(before + g.getGenre());
                                // oznaci doplnene
                                dialog.getInputGenres().setSelectionStart(before.length() + start);
                                dialog.getInputGenres().setSelectionEnd(dialog.getInputGenres().getText().length());
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    private class NewBookActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {

                case "saveButton":
                    saveBook();
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
                        dialog.getInputBuyedDate().setText(DateHelper.dateToString(d, false));
                    }

                    break;
                default:
                    break;

            }
        }

        private void saveBook() {
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
                if (Configuration.getInstance().isRequireAuthor()) {
                    errorOutput.append("- Vyplňte autora \n");
                }
            } else {
                ArrayList<Author> temp = new ArrayList<>();
                for (int i = 0; i < authorListModel.getSize(); i++) {
                    temp.add(authorListModel.get(i));
                }
                b.setAuthors(temp);
                b.setMainAuthor(temp.get(0));
            }

            //vydavatel 
            if (dialog.getInputPublisher().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequirePublisher()) {
                    errorOutput.append("- Vyplňte vydavatele \n");
                }
            } else {
                b.setPublisher(dialog.getInputPublisher().getText());
            }

            //jazyk
            if (dialog.getInputLanguage().getText().trim().isEmpty()) {

                if (Configuration.getInstance().isRequireLanguage()) {
                    errorOutput.append("- Vyplňte jazyk \n");
                }
            } else {
                b.setLanguage(dialog.getInputLanguage().getText());
            }

            //rok vydání
            Date d = (Date) dialog.getInputPublishedDate().getValue();

            if (d == null) {
                if (Configuration.getInstance().isRequirePublishedYear()) {
                    errorOutput.append("- Vyplňte rok vydání \n");
                }
            } else {
                b.setPublishedYear(d);
            }

            // ISBN 10
            if (dialog.getInputISBN10().getText() == null || dialog.getInputISBN10().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireISBN10()) {
                    errorOutput.append("- Vyplňte ISBN 10\n");
                }
            } else {
                b.setISBN10(dialog.getInputISBN10().getText());
            }

            // ISBN 13
            if (dialog.getInputISBN13().getText() == null || dialog.getInputISBN13().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireISBN13()) {
                    errorOutput.append("- vyplňte ISBN 13\n");
                }
            } else {
                b.setISBN13(dialog.getInputISBN13().getText());
            }

            // datum pridani
            Date d2 = DateHelper.stringToDate(dialog.getInputBuyedDate().getText(), false);
            if (d2 == null) {
                if (Configuration.getInstance().isRequireAddedDate()) {
                    errorOutput.append("- Zkontrolujte zadané datum přidání\n");
                }
            } else {
                b.setAddedDate(d2);
            }

            // Umístění 
            if (dialog.getInputLocation().getText() == null || dialog.getInputLocation().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireLocation()) {
                    errorOutput.append("Vyplňte prosím umístění\n");
                }
            } else {
                b.setLocation(dialog.getInputLocation().getText());
            }

            b.setPageCount((int) dialog.getInputPageCount().getValue());

            //zanry
            if (!dialog.getInputGenres().getText().trim().isEmpty()) {
                List<Genre> genres = new ArrayList<>();

                String[] tempGenres = dialog.getInputGenres().getText().split(";");

                for (String s : tempGenres) {
                    Genre g = GenreService.getInstance().findGenre(s);
                    if (g == null) {
                        errorOutput.append("žánr ").append(s).append(" neexistuje - přidat jej můžete v nastavení");
                        break;
                    }
                    genres.add(g);
                }
                if (genres.isEmpty() && Configuration.getInstance().isRequireGenre()) {
                    errorOutput.append("Vyplňte žánr");
                } else {
                    b.setGenres(genres);
                }
            } else {
                if (Configuration.getInstance().isRequireGenre()) {
                    errorOutput.append("Vyplňte žánr");
                }
            }


            // Check validation results
            if (errorOutput.length() == 0) {
                BookService.getInstance().saveBook(b, (int) dialog.getInputCount().getValue());
                // UPDATE TABULKY
                dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, errorOutput.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
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
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Zamezí opětovnému spuštění
            dialog.getSearchButton().setEnabled(false);
            dialog.getSearchButton().setText("Hledám...");
            dialog.getSearchButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/waiting.gif")));

            // Nastavý hodnoty pro vyhledávání
            for (int i = 0; i < authorListModel.getSize(); i++) {
                gbs.setAutor(authorListModel.get(i).toString());
            }
            gbs.setTitle(dialog.getInputTitle().getText());
            gbs.setISBN(dialog.getInputISBN13().getText());
            gbs.setISBN(dialog.getInputISBN10().getText());

            if (gbs.getQuery().isEmpty()) {
                dialog.getSearchButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));
                dialog.getSearchButton().setText("Hledat znovu");
                dialog.getSearchButton().setEnabled(true);
                return;
            }

            // Vyhledávání
            gbs.start();
        }
    }
}
