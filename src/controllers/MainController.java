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
import javax.swing.JTable;
import models.CatalogTableModel;
import views.FilterTableDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {

    private MainView mainView;
    private MenuController menuController;
    private FilterTableDialog filter;
    private CatalogTableModel tableModel;

    public MainController() {
        mainView = new MainView();
        menuController = new MenuController(this, mainView);
        tableModel = new CatalogTableModel();
        filter = new FilterTableDialog(mainView, true);
        initListeners();
        updateView();
    }

    private void initListeners() {
        mainView.getCatalogTable().addMouseListener(new TableMouseListener());

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
    }
    
    public void tableDataChanged(){
        tableModel.updateData();
        tableModel.fireTableDataChanged();
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
                String out = t.getValueAt(t.rowAtPoint(p), 0).toString();
                System.out.println(out);
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

    public CatalogTableModel getCatalogTableModel() {
        return tableModel;
    }
}
