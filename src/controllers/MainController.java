/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.CatalogTableModel;
import models.entity.Book;
import services.GoogleBooksSearch;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {

    private MainView mainView;
    private NewItemController newItemController;

    public MainController() {
        mainView = new MainView();
        initListeners();
        setData();
    }

    private void initListeners() {
        mainView.getNewItemButton().addActionListener(new NewItemButtonClickedListener());
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

    private class NewItemButtonClickedListener implements ActionListener {

        public NewItemButtonClickedListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            newItemController = new NewItemController(mainView);
            newItemController.showView();
        }
    }
}
