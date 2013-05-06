/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.JComponent;
import models.BookTableModel;
import models.entity.Book;
import services.BookService;
import views.BookFilterDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class BookTabController {

    private MainView mainView;
    private BookFilterDialog filter;
    private BookTableModel tableModel;
    Map<String, String> orderType = BookService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = BookService.getInstance().getOrderByMap();

    public BookTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new BookTableModel();
        mainView.getCatalogTable().setModel(tableModel);

        // FilterDialog
        filter = new BookFilterDialog(mainView, true);
        setFilterData();

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

    public void updateView() {
        // UPDATE DATA
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            mainView.getBookTablePrevButton().setEnabled(false);
        } else {
            mainView.getBookTablePrevButton().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            mainView.getBookTableNextButton().setEnabled(false);
        } else {
            mainView.getBookTableNextButton().setEnabled(true);
        }
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
                            tableModel.applyFilter("");
                            tableModel.updateData();
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
                    updateView();
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
