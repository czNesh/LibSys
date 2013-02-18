/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import models.entity.Author;
import models.entity.Book;
import models.entity.Customer;
import services.AuthorService;
import services.BookService;
import views.BookListDialog;

/**
 *
 * @author Nesh
 */
public class BookListController extends BaseController {

    private BookListDialog dialog;
    private ArrayList<Book> selectedBooks;

    public BookListController(JFrame parent, boolean selectionMode) {
        dialog = new BookListDialog(parent, selectionMode);
        initListeners();
    }

    private void initListeners() {
        // ActionListener
        BookListButtonListener b = new BookListButtonListener();
        dialog.getConfirmButton().addActionListener(b);
        dialog.getCancelButton().addActionListener(b);
        dialog.getFilterButton().addActionListener(b);
        dialog.getSearchButton().addActionListener(b);

        // KeyListener
        BookListKeyListener k = new BookListKeyListener();
        dialog.getInputBarcode().addKeyListener(k);
        dialog.getInputTitle().addKeyListener(k);
        dialog.getInputAuthor().addKeyListener(k);
        dialog.getInputISBN10().addKeyListener(k);
        dialog.getInputISBN13().addKeyListener(k);

        // MouseListener
        BookListMouseListener m = new BookListMouseListener();
        dialog.getResultTable().addMouseListener(m);
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

    private class BookListMouseListener implements MouseListener {

        public BookListMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
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

    private class BookListKeyListener implements KeyListener {
        
        private List<Book> books;
        private List<Author> authors;

        public BookListKeyListener() {
            books = BookService.getInstance().getBooks();
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

            // ESCAPE pri stisku sipky
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                return;
            }
            
                        // priprava promennych 
            String in;
            int start;

            // co se doplňuje 
            switch (((JComponent) e.getSource()).getName()) {

                // doplnění jména
                case "barcode":
                    in = dialog.getInputBarcode().getText().trim();
                    start = in.length();

                    // zadano aspon 5 znaku
                    if (start > 4) {
                        for (Book b : books) {
                            if (b.getBarcode().startsWith(in)) {
                                dialog.getInputBarcode().setText(b.getBarcode());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputBarcode().setSelectionStart(start);
                    dialog.getInputBarcode().setSelectionEnd(dialog.getInputBarcode().getText().length());
                    break;

                // doplnění přijmení    
                case "title":
                    in = dialog.getInputTitle().getText().trim();
                    start = in.length();

                    // zadano aspon 5 znaku
                    if (start > 4) {
                        for (Book b : books) {
                            if (b.getTitle().startsWith(in)) {
                                dialog.getInputTitle().setText(b.getTitle());
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputTitle().setSelectionStart(start);
                    dialog.getInputTitle().setSelectionEnd(dialog.getInputTitle().getText().length());
                    break;

                case "author":
                    in = dialog.getInputAuthor().getText().trim();
                    start = in.length();

                    // zadany aspon 4 znaky
                    if (start > 6) {
                        for (Author a : authors) {
                            if (String.valueOf(a.toString()).startsWith(in)) {
                                dialog.getInputAuthor().setText(String.valueOf(a.toString()));
                                break;
                            }
                        }
                    }

                    // oznaci doplnene
                    dialog.getInputAuthor().setSelectionStart(start);
                    dialog.getInputAuthor().setSelectionEnd(dialog.getInputAuthor().getText().length());

                    break;
                default:
                    break;

            }
        }
    }

    private class BookListButtonListener implements ActionListener {

        public BookListButtonListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }

    public ArrayList<Book> getBooks() {
        return selectedBooks;
    }
}
