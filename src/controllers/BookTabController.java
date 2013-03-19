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
import javax.swing.JComponent;
import models.BookTableModel;
import models.entity.Book;
import views.BookFilterDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
class BookTabController {

    private MainView mainView;
    private BookFilterDialog filter;
    private BookTableModel tableModel;

    public BookTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new BookTableModel();
        mainView.getCatalogTable().setModel(tableModel);

        // FilterDialog
        filter = new BookFilterDialog(mainView, true);

        // INIT LISTENERS
        initListeners();

        // update view
        updateView();
    }

    private void initListeners() {
        // MouseListeners
        mainView.getCatalogTable().addMouseListener(new BookTabMouseListener());

        // ActionListener
        BookTabActionListener a = new BookTabActionListener();
        mainView.getBookTableNextButton().addActionListener(a);
        mainView.getBookTablePrevButton().addActionListener(a);
        mainView.getFilterButton().addActionListener(a);

        // KeyListener
        BookTabKeyListener b = new BookTabKeyListener();
        mainView.getBookTableInputNumber().addKeyListener(b);
        mainView.getBookFilterInput().addKeyListener(b);

        // FILTER Listeners
        filter.getOkButton().addActionListener(a);

    }

    private void updateView() {
        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));
    }

    private class BookTabKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "bookFilter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getBookFilterInput().getText().trim().isEmpty()) {
                            tableModel.resetFilter();
                        } else {
                            tableModel.applyFilter(mainView.getBookFilterInput().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "bookPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getBookTableInputNumber().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class BookTabActionListener implements ActionListener {

        public BookTabActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "prevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
                case "filter":
                    filter.setLocationRelativeTo(null);
                    filter.setVisible(true);
                    tableModel.setViewSettings(
                            filter.getTitleCheckbox().isSelected(),
                            filter.getAuthorCheckbox().isSelected(),
                            filter.getPublisherCheckbox().isSelected(),
                            filter.getPublishedDateCheckbox().isSelected(),
                            filter.getLanguageCheckbox().isSelected(),
                            filter.getISBN10Checkbox().isSelected(),
                            filter.getISBN13Checkbox().isSelected(),
                            filter.getPageCountCheckbox().isSelected(),
                            filter.getCountCheckbox().isSelected(),
                            filter.getLocationCheckbox().isSelected());
                    updateView();
                    filter.setVisible(false);
                    break;

                case "filterConfirm":
                    filter.setVisible(false);
                    break;
                default:
                    break;
            }
        }
    }

    private class BookTabMouseListener implements MouseListener {

        public BookTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Book b = (Book) tableModel.getBook(mainView.getCatalogTable().getSelectedRow());
                BookDetailController bdc = new BookDetailController(b);
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

    public BookTableModel getCatalogTableModel() {
        return tableModel;
    }
}
