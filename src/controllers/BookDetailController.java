/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import models.entity.Author;
import models.entity.Book;
import remote.GoogleService;
import views.BookDetailDialog;

/**
 *
 * @author Nesh
 */
public class BookDetailController extends BaseController {

    private BookDetailDialog dialog;
    Book book;

    public BookDetailController(Book book) {
        dialog = new BookDetailDialog(null, true);
        this.book = book;
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
        dialog.getInfoStock().setText(String.valueOf(book.getCount() - book.getBorrowedCount()) + "/" + String.valueOf(book.getCount()));
        dialog.getInfoLocation().setText(book.getLocation());


        // Nahled 
        GoogleService gs = new GoogleService();
        gs.setISBN(book.getISBN10());
        gs.setISBN(book.getISBN13());
        if (gs.getThumbnailURL() != null) {
            try {
                URL url = new URL(gs.getThumbnailURL());
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.21; Mac_PowerPC)");
                InputStream in = conn.getInputStream();
                Image image = ImageIO.read(in);
                image = image.getScaledInstance(173, -1, Image.SCALE_DEFAULT);
                image = image.getScaledInstance(-1, 263, Image.SCALE_DEFAULT);
                dialog.getInfoThumb().setIcon(new ImageIcon(image));
                dialog.getInfoThumb().setText("");
                
            } catch (IOException ex) {
                Logger.getLogger(BookDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            dialog.getInfoThumb().setText(":-( Náhled není k dispozici");
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
