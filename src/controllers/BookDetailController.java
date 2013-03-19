/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import models.BorrowTableModel;
import models.entity.Author;
import models.entity.Book;
import models.entity.Borrow;
import remote.GoogleImageDownload;
import services.BookService;
import services.BorrowService;
import views.BookDetailDialog;

/**
 *
 * @author Nesh
 */
public class BookDetailController extends BaseController {

    private BookDetailDialog dialog;
    private BorrowTableModel tableModel;
    Book book;

    public BookDetailController(Book book) {
        dialog = new BookDetailDialog(null, true);
        this.book = book;
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfBook(book));
        initListeners();
        showData();

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
        BookDetailActionListener a = new BookDetailActionListener();
        dialog.getEditButton().addActionListener(a);
        dialog.getCloseButton().addActionListener(a);

        //MouseListener
        BookDetailListListener m = new BookDetailListListener();
        dialog.getLastBorrowTable().addMouseListener(m);
    }

    private void showData() {
        // Titul
        dialog.getInfoTitle().setText(book.getTitle());

        // Autori
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Author a : book.getAuthors()) {
            if (first) {
                sb.append(a.toString());
                first = false;
                continue;
            }
            sb.append(("; " + a.toString()));
        }
        dialog.getInfoAuthors().setText(sb.toString());

        // Jazyk
        dialog.getInfoLanguage().setText(book.getLanguage());

        // Vydavatel
        dialog.getInfoPublisher().setText(book.getPublisher());

        // Vydáno roku
        if (book.getPublishedYear() != null) {
            dialog.getInfoPublishedYear().setText(DateFormater.dateToString(book.getPublishedYear(), true));
        } else {
            dialog.getInfoPublishedYear().setText("-");
        }

        // ISBN 10
        dialog.getInfoISBN10().setText(book.getISBN10());

        //ISBN 13
        dialog.getInfoISBN13().setText(book.getISBN13());

        // Pocet stran
        dialog.getInfoPageCount().setText(String.valueOf(book.getPageCount()));

        // Zanr
        //dialog.getInfoGenre().setText(book.getGenres());

        // Sponzor
        dialog.getInfoSponsor().setText(book.getSponsor());

        // Zakoupeno dne
        dialog.getInfoBuyedDate().setText(DateFormater.dateToString(book.getAddedDate(), false));

        // Poznamky
        dialog.getInfoNotes().setText(book.getNotes());

        // Sklad
        int borrowed = BookService.getInstance().getBorrowed(book.getVolumeCode());
        int count = BookService.getInstance().getCount(book.getVolumeCode());

        dialog.getInfoStock().setText(String.valueOf(count - borrowed) + "/" + String.valueOf(count));
        dialog.getInfoLocation().setText(book.getLocation());

        // Nahled 
        GoogleImageDownload gid = new GoogleImageDownload(dialog.getInfoThumb(), 173, 263);
        gid.setISBN(book.getISBN10());
        gid.start();

        // Posledni výpůjčky
        dialog.getLastBorrowTable().setModel(tableModel);
    }

    private class BookDetailListListener implements MouseListener {

        public BookDetailListListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(dialog.getLastBorrowTable().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
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

    private class BookDetailActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "editButton":
                    System.out.println("TO DO");
                    break;
                case "closeButton":
                    dispose();
                    break;
                default:
                    break;
            }
        }
    }
}
