/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import models.BookTableModel;
import models.entity.Author;
import models.entity.Book;
import services.AuthorService;
import services.BookService;
import views.BookFilterDialog;
import views.BookListDialog;

/**
 *
 * @author Nesh
 */
public class BookListController extends BaseController {

    private BookListDialog dialog;
    private ArrayList<Book> selectedBooks = new ArrayList<>();
    private boolean selectionMode;
    private BookTableModel tableModel;
    private BookFilterDialog filterDialog;

    public BookListController(JFrame parent, boolean selectionMode) {
        dialog = new BookListDialog(parent, selectionMode);
        this.selectionMode = selectionMode;
        tableModel = new BookTableModel();


        dialog.getResultTable().setModel(tableModel);
        filterDialog = new BookFilterDialog(null, true);
        setVisibility(true, true, false, true, false, false, false, false, true, true);
        initListeners();
        updateView();


    }

    private void initListeners() {
        // ActionListener
        BookListActionListener a = new BookListActionListener();
        dialog.getConfirmButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);
        dialog.getFilterButton().addActionListener(a);
        dialog.getSearchButton().addActionListener(a);
        dialog.getBookTablePrevButton().addActionListener(a);
        dialog.getBookTableNextButton().addActionListener(a);

        // KeyListener
        BookListKeyListener k = new BookListKeyListener();
        dialog.getInputBarcode().addKeyListener(k);
        dialog.getInputTitle().addKeyListener(k);
        dialog.getInputAuthor().addKeyListener(k);
        dialog.getInputISBN10().addKeyListener(k);
        dialog.getInputISBN13().addKeyListener(k);
        dialog.getBookTableInputNumber().addKeyListener(k);

        // MouseListener
        BookListMouseListener m = new BookListMouseListener();
        dialog.getResultTable().addMouseListener(m);
    }

    private void updateView() {
        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        dialog.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        dialog.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));
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

    private void setVisibility(boolean showTitle, boolean showAuthor, boolean showPublisher, boolean showPublishedYear, boolean showlanguage, boolean showISBN10, boolean showISBN13, boolean showPageCount, boolean showItemCount, boolean showLocation) {
        tableModel.setVisibility(showTitle, showAuthor, showPublisher, showPublishedYear, showlanguage, showISBN10, showISBN13, showPageCount, showItemCount, showLocation);

        filterDialog.getTitleCheckbox().setSelected(showTitle);
        filterDialog.getAuthorCheckbox().setSelected(showAuthor);
        filterDialog.getPublisherCheckbox().setSelected(showPublisher);
        filterDialog.getPublishedDateCheckbox().setSelected(showPublishedYear);
        filterDialog.getLanguageCheckbox().setSelected(showlanguage);
        filterDialog.getISBN10Checkbox().setSelected(showISBN10);
        filterDialog.getISBN13Checkbox().setSelected(showISBN13);
        filterDialog.getPageCountCheckbox().setSelected(showPageCount);
        filterDialog.getCountCheckbox().setSelected(showItemCount);
        filterDialog.getLocationCheckbox().setSelected(showLocation);
    }

    private class BookListMouseListener implements MouseListener {

        public BookListMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                selectedBooks.add((Book) tableModel.getBook(dialog.getResultTable().getSelectedRow()));
            }
            if (selectionMode) {
                if (!selectedBooks.isEmpty()) {
                    dialog.dispose();
                }
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
            String sourceName = ((JComponent) e.getSource()).getName();

            if (e.getKeyCode() == KeyEvent.VK_ENTER && sourceName.equals("inputPageNumber")) {
                String in = dialog.getBookTableInputNumber().getText();
                try {
                    tableModel.setPage(Integer.parseInt(in));
                } catch (NumberFormatException ex) {
                    System.out.println("NESPRAVNY FORMAT CISLA");
                }
                updateView();
            }

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
            switch (sourceName) {

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

    private class BookListActionListener implements ActionListener {

        public BookListActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            String buttonName = b.getName();

            switch (buttonName) {
                case "confirm":
                    if (dialog.getResultTable().getSelectedRows().length > 0 && selectionMode) {
                        int[] selRows = dialog.getResultTable().getSelectedRows();
                        for (int i = 0; i < selRows.length; i++) {
                            selectedBooks.add(tableModel.getBook(selRows[i]));
                        }
                        dialog.dispose();
                    }

                    break;
                case "cancel":
                    dispose();
                    break;

                case "filter":
                    filterDialog.getOkButton().addActionListener(this);
                    filterDialog.setLocationRelativeTo(null);
                    filterDialog.setVisible(true);
                    break;

                case "filterConfirm":
                    tableModel.setVisibility(
                            filterDialog.getTitleCheckbox().isSelected(),
                            filterDialog.getAuthorCheckbox().isSelected(),
                            filterDialog.getPublisherCheckbox().isSelected(),
                            filterDialog.getPublishedDateCheckbox().isSelected(),
                            filterDialog.getLanguageCheckbox().isSelected(),
                            filterDialog.getISBN10Checkbox().isSelected(),
                            filterDialog.getISBN13Checkbox().isSelected(),
                            filterDialog.getPageCountCheckbox().isSelected(),
                            filterDialog.getCountCheckbox().isSelected(),
                            filterDialog.getLocationCheckbox().isSelected());
                    tableModel.fireTableStructureChanged();
                    filterDialog.setVisible(false);
                    break;

                case "search":
                    tableModel.setFilter(
                            dialog.getInputBarcode().getText().trim(),
                            dialog.getInputTitle().getText().trim(),
                            dialog.getInputAuthor().getText().trim(),
                            dialog.getInputISBN10().getText().trim(),
                            dialog.getInputISBN13().getText().trim(),
                            DateFormater.stringToDate(dialog.getInputPublishedYear().getText(), true));
                    tableModel.fireTableDataChanged();
                    break;

                case "prevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                default:
                    System.out.println("Chyba - Jmeno polozky neodpovida zadne operaci (Buttonlistener)");
            }
        }
    }

    public ArrayList<Book> getBooks() {
        return selectedBooks;
    }
}
