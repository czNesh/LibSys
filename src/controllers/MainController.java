/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JTable;
import models.BookTableModel;
import models.entity.Book;
import views.BookFilterDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {
    
    private MainView mainView;
    private MenuController menuController;
    private BookFilterDialog filter;
    private BookTableModel tableModel;
    
    public MainController() {
        mainView = new MainView();
        menuController = new MenuController(this, mainView);
        tableModel = new BookTableModel();
        filter = new BookFilterDialog(mainView, true);
        initListeners();
        updateView();
    }
    
    private void initListeners() {
        // Table Listeners
        mainView.getCatalogTable().addMouseListener(new TableMouseListener());
        BookTableActionListener bt = new BookTableActionListener();
        mainView.getBookTableNextButton().addActionListener(bt);
        mainView.getBookTablePrevButton().addActionListener(bt);
        mainView.getBookTableInputNumber().addKeyListener(new BookTableKeyListener());

        // FILTER LISTENERS 
        mainView.getFilterButton().addActionListener(new FilterButtonListener());
        filter.getOkButton().addActionListener(new FilterOKButtonListener());
    }
    
    @Override
    void showView() {
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }
    
    @Override
    void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void updateView() {
        // SET USER NAME
        mainView.getSystemUserLabel().setText(AppController.getInstance().getLoggedUser().toString());

        // FILL TABLE
        mainView.getCatalogTable().setModel(tableModel);
        tableModel.fireTableStructureChanged();

        // GET PAGE 
        mainView.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));
    }
    
    public void tableDataChanged() {
        tableModel.updateData();
        tableModel.fireTableDataChanged();
    }
    
    private class BookTableKeyListener implements KeyListener {
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String in = mainView.getBookTableInputNumber().getText();
                try {
                    tableModel.setPage(Integer.parseInt(in));
                } catch (NumberFormatException ex) {
                    System.out.println("NESPRAVNY FORMAT CISLA");
                }
                updateView();
            }
        }
    }
    
    private class BookTableActionListener implements ActionListener {
        
        public BookTableActionListener() {
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "bookTableNextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "bookTablePrevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
            }
        }
    }
    
    private class FilterOKButtonListener implements ActionListener {
        
        public FilterOKButtonListener() {
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            filter.setVisible(false);
        }
    }
    
    private class FilterButtonListener implements ActionListener {
        
        public FilterButtonListener() {
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            filter.setLocationRelativeTo(null);
            filter.show();
            tableModel.setVisibility(
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
        }
    }
    
    private class TableMouseListener implements MouseListener {
        
        public TableMouseListener() {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();
                JTable t = mainView.getCatalogTable();
                Book b = (Book) tableModel.getBook(t.getSelectedRow());
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
