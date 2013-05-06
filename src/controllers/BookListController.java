/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private BookFilterDialog filter;
    Map<String, String> orderType = BookService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = BookService.getInstance().getOrderByMap();

    public BookListController(JFrame parent, boolean selectionMode) {
        dialog = new BookListDialog(parent, selectionMode);
        this.selectionMode = selectionMode;
        tableModel = new BookTableModel();
        dialog.getResultTable().setModel(tableModel);
        filter = new BookFilterDialog(null, true);
        setFilterData();
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
        // UPDATE DATA
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        dialog.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        dialog.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            dialog.getBookTablePrevButton().setEnabled(false);
        } else {
            dialog.getBookTablePrevButton().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            dialog.getBookTableNextButton().setEnabled(false);
        } else {
            dialog.getBookTableNextButton().setEnabled(true);
        }
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

    private void setFilterData() {
        filter.getTitleCheckbox().setSelected(Configuration.getInstance().isShowTitle());
        filter.getAuthorCheckbox().setSelected(Configuration.getInstance().isShowAuthor());
        filter.getISBN10Checkbox().setSelected(Configuration.getInstance().isShowISBN10());
        filter.getISBN13Checkbox().setSelected(Configuration.getInstance().isShowISBN13());
        filter.getCountCheckbox().setSelected(Configuration.getInstance().isShowCount());
        filter.getLocationCheckbox().setSelected(Configuration.getInstance().isShowLocation());
        filter.getLanguageCheckbox().setSelected(Configuration.getInstance().isShowLanguage());
        filter.getPublisherCheckbox().setSelected(Configuration.getInstance().isShowPublisher());
        filter.getPublishedDateCheckbox().setSelected(Configuration.getInstance().isShowPublishedYear());
        filter.getPageCountCheckbox().setSelected(Configuration.getInstance().isShowPageCount());

        filter.getINPbookMaxRowsCount().setValue((int) Configuration.getInstance().getMaxBookRowsCount());

        for (Map.Entry<String, String> entry : orderType.entrySet()) {
            filter.getINPorderType().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBookOrderType())) {
                filter.getINPorderType().setSelectedItem(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : orderBy.entrySet()) {
            filter.getINPorderBy().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBookOrderBy())) {
                filter.getINPorderBy().setSelectedItem(entry.getKey());
            }
        }


        RefreshController.getInstance().refreshBookTab();
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
                    filter.getOkButton().addActionListener(this);
                    filter.setLocationRelativeTo(null);
                    filter.setVisible(true);
                    break;

                case "filterConfirm":
                    Configuration.getInstance().setShowTitle(filter.getTitleCheckbox().isSelected());
                    Configuration.getInstance().setShowAuthor(filter.getAuthorCheckbox().isSelected());
                    Configuration.getInstance().setShowPublisher(filter.getPublisherCheckbox().isSelected());
                    Configuration.getInstance().setShowPublishedYear(filter.getPublishedDateCheckbox().isSelected());
                    Configuration.getInstance().setShowLanguage(filter.getLanguageCheckbox().isSelected());
                    Configuration.getInstance().setShowISBN10(filter.getISBN10Checkbox().isSelected());
                    Configuration.getInstance().setShowISBN13(filter.getISBN13Checkbox().isSelected());
                    Configuration.getInstance().setShowPageCount(filter.getPageCountCheckbox().isSelected());
                    Configuration.getInstance().setShowCount(filter.getCountCheckbox().isSelected());
                    Configuration.getInstance().setShowLocation(filter.getLocationCheckbox().isSelected());

                    Configuration.getInstance().setBookOrderBy(orderBy.get((String) filter.getINPorderBy().getSelectedItem()));
                    Configuration.getInstance().setBookOrderType(orderType.get((String) filter.getINPorderType().getSelectedItem()));
                    Configuration.getInstance().setMaxBookRowsCount((int) filter.getINPbookMaxRowsCount().getValue());
                    tableModel.updateData();
                    tableModel.fireTableStructureChanged();
                    filter.setVisible(false);
                    break;

                case "search":
                    tableModel.search(
                            dialog.getInputBarcode().getText().trim(),
                            dialog.getInputTitle().getText().trim(),
                            dialog.getInputAuthor().getText().trim(),
                            dialog.getInputISBN10().getText().trim(),
                            dialog.getInputISBN13().getText().trim(),
                            dialog.getInputPublishedYear().getText().trim());
                    updateView();
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
