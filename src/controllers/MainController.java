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
    private MenuController menuController;
    private BookTabController bookTabController;
    private BorrowTabController borrowTabController;
    private NotificationTabController notificationTabController;
    
    public MainController() {
        // Hlavní pohled
        mainView = new MainView();

        // Menu controller
        menuController = new MenuController(mainView);
                
        // Ostatní controller(y)
        bookTabController = new BookTabController(mainView);
        borrowTabController = new BorrowTabController(mainView);
        notificationTabController = new NotificationTabController(mainView);

        updateView();
    }
    
    @Override
    void showView() {
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }
    
    @Override
    void dispose() {
        mainView.dispose();
        mainView = null;
    }
    
    private void updateView() {
        // SET USER NAME
        mainView.getSystemUserLabel().setText(AppController.getInstance().getLoggedUser().toString());
    } 
}