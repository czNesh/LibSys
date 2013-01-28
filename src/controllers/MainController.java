/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import models.CatalogTableModel;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {

    private MainView mainView;
    private MenuController menuController;

    public MainController() {
        mainView = new MainView();
        menuController = new MenuController(this, mainView);
        initListeners();
        setData();
    }

    private void initListeners() {
        mainView.getCatalogTable().addMouseListener(new TableMouseListener());
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

    private void setData() {
        // SET USER NAME
        mainView.getSystemUserLabel().setText(AppController.getInstance().getLoggedUser().toString());

        // FILL TABLE
        CatalogTableModel tableModel = new CatalogTableModel();
        mainView.getCatalogTable().setModel(tableModel);
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
}
