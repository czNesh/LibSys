/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.CatalogTableModel;
import models.dao.BookDAO;
import models.entity.Author;
import models.entity.Book;
import services.DateFormater;
import services.GoogleBooksSearch;
import views.DatePicker;
import views.NewItemDialog;
import views.SearchResultsDialog;

/**
 *
 * @author Administrator
 */
public class NewItemController extends BaseController {

    private NewItemDialog dialog;
    private SearchResultsDialog resOut;
    private GoogleBooksSearch gbs;
    private Book selectedBoook;
    private ArrayList<Book> foundedItems;
    private MainController mc;

    public NewItemController(JFrame parent, MainController mc) {
        this.mc = mc;
        gbs = new GoogleBooksSearch();
        dialog = new NewItemDialog(parent, false);
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
        dialog.getSearchButton().addActionListener(new SearchButtonListener());
        dialog.getSaveButton().addActionListener(new SaveButtonListener());
        dialog.getDatePickerButton1().addActionListener(new DatePickerButton());
        dialog.getDatePickerButton2().addActionListener(new DatePickerButton());
    }

    private class SaveButtonListener implements ActionListener {

        public SaveButtonListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Book b = new Book();
            JLabel errorOutput = dialog.getErrorMessageOutput();

            // TITUL
            if (dialog.getInputTitle().getText() == null || dialog.getInputTitle().getText().trim().isEmpty()) {
                errorOutput.setText("Titul je poviná položka");
            } else {
                b.setTitle(dialog.getInputTitle().getText());
            }

            // AUTORI
            if (dialog.getInputAuthor().getText() == null || dialog.getInputAuthor().getText().trim().isEmpty()) {
                errorOutput.setText("Autori je poviná položka");
            } else {
                String tempAuthors = dialog.getInputAuthor().getText();
                String[] autorsArray = tempAuthors.split(";");
                List<Author> authorList = new ArrayList<>();

                for (int i = 0; i < autorsArray.length; i++) {
                    // VYTVORI POTREBNE PROMENE
                    Author tempAuthor = new Author();
                    String fname = "";
                    String lname;

                    String[] foo = autorsArray[i].split(" ");

                    for (int j = 0; j < foo.length - 1; j++) {
                        fname += foo[j];
                    }
                    lname = foo[foo.length - 1];
                    tempAuthor.setFirstName(fname);
                    tempAuthor.setLastName(lname);
                    authorList.add(tempAuthor);
                }

                b.setAuthors(authorList);
                b.setMainAuthor(authorList.get(0));
            }

            // ISBN 10
            if (dialog.getInputISBN10().getText() == null || dialog.getInputISBN10().getText().trim().isEmpty()) {
                errorOutput.setText("ISBN10 je poviná položka");
            } else {
                b.setISBN10(dialog.getInputISBN10().getText());
            }

            // ISBN 13
            if (dialog.getInputISBN13().getText() == null || dialog.getInputISBN13().getText().trim().isEmpty()) {
                errorOutput.setText("ISBN13 je poviná položka");
            } else {
                b.setISBN13(dialog.getInputISBN13().getText());
            }

            // rok vydani
            if (dialog.getInputPublishedDate().getText() == null || dialog.getInputPublishedDate().getText().trim().isEmpty()) {
                errorOutput.setText("rok vydani je poviná položka");
            } else {
                b.setPublishedYear(DateFormater.stringToDate(dialog.getInputBuyedDate().getText(), false));
            }

            // datum pridani
            if (dialog.getInputBuyedDate().getText() == null || dialog.getInputBuyedDate().getText().trim().isEmpty()) {
                errorOutput.setText("datum pridani je poviná položka");
            } else {
                b.setAddedDate(DateFormater.stringToDate(dialog.getInputBuyedDate().getText(), false));
            }

            BookDAO.getInstance().save(b);
            mc.tableDataChanged();
            dispose();
        }
    }

    private class DatePickerButton implements ActionListener {

        public DatePickerButton() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DatePicker dp = new DatePicker(null, true);
            dp.setLocationRelativeTo(null);
            dp.setVisible(true);

            JComponent b = (JComponent) e.getSource();
            String name = b.getName();
            if (dp.getDate() != null) {
                Date d = dp.getDate();

                switch (name) {
                    case "publishedPickerButton":
                        dialog.getInputPublishedDate().setText(DateFormater.dateToString(d, true));
                        break;
                    case "buyedPickerButton":

                        dialog.getInputBuyedDate().setText(DateFormater.dateToString(d, false));
                        break;
                    default:
                        System.out.println("Operace nerozpoznána");
                }
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
                if (selectedBoook.getMainAuthor() != null) {
                    dialog.getInputAuthor().setText(selectedBoook.getMainAuthor().toString());
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
                    dialog.getInputPublishedDate().setText(String.valueOf(selectedBoook.getPublishedYear().getYear()));
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
            gbs = new GoogleBooksSearch();
            gbs.addChangeListener(new SearchChangeListener());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gbs.setAutor(dialog.getInputAuthor().getText());
            gbs.setTitle(dialog.getInputTitle().getText());
            gbs.setISBN(dialog.getInputISBN13().getText());
            gbs.setISBN(dialog.getInputISBN10().getText());
            gbs.search();
            System.out.println(gbs.getResultsCount());
            if (gbs.getResultsCount() == 0) {
                System.out.println("NOT MATCHES");
            } else {
                resOut = new SearchResultsDialog(null, true);
                resOut.getResultsTable().addMouseListener(new TableClickedMouseListener());
                foundedItems = new ArrayList<>();

                ArrayList<Book> r = gbs.getResults();
                for (int i = 0; i < r.size(); i++) {
                    foundedItems.add((Book) r.get(i));
                }

                CatalogTableModel ctm = new CatalogTableModel(foundedItems);
                resOut.getResultsTable().setModel(ctm);
                resOut.setLocationRelativeTo(null);
                resOut.setVisible(true);
            }
        }
    }

    private class SearchChangeListener implements ChangeListener {

        public SearchChangeListener() {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            dialog.getSearchProgressBar().setValue(gbs.getStatus());
            dialog.getSearchProgressBar().setToolTipText(gbs.getTextStatus());
        }
    }
}
