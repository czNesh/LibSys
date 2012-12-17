/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
}
