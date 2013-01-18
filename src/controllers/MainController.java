/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.CatalogTableModel;
import models.dao.CatalogItemDAO;
import models.dao.SystemUserDAO;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class MainController extends BaseController {

    private MainView mainView;

    public MainController() {
        mainView = new MainView();
        initListeners();
        setData();
    }
    
    private void initListeners(){
        
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
    
    
    
}
