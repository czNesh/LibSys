/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author petr.hejhal
 */
public class RefreshController {

    public static RefreshController instance;
    private BookTabController bookTabController;
    private CustomerTabController customerTabController;
    private BorrowTabController borrowTabController;
    private NotificationTabController notificationTabController;

    public static RefreshController getInstance() {
        synchronized (RefreshController.class) {
            if (instance == null) {
                instance = new RefreshController();
            }
        }
        return instance;
    }

    private RefreshController() {
        // SINGLETON
    }

    public void setBookTabController(BookTabController bookTabController) {
        this.bookTabController = bookTabController;
    }

    public void setCustomerTabController(CustomerTabController customerTabController) {
        this.customerTabController = customerTabController;
    }

    public void setBorrowTabController(BorrowTabController borrowTabController) {
        this.borrowTabController = borrowTabController;
    }

    public void setNotificationTabController(NotificationTabController notificationTabController) {
        this.notificationTabController = notificationTabController;
    }

    public void setControllers(BookTabController bookTabController, CustomerTabController customerTabController, BorrowTabController borrowTabController, NotificationTabController notificationTabController) {
        this.bookTabController = bookTabController;
        this.customerTabController = customerTabController;
        this.borrowTabController = borrowTabController;
        this.notificationTabController = notificationTabController;
    }

    public void refreshBookTab() {
        if (bookTabController == null) {
            return;
        }
        System.out.println("Refresh book");
        bookTabController.updateView();
    }

    public void refreshCustomerTab() {

        if (customerTabController == null) {
            return;
        }
        System.out.println("Refresh customer");
        customerTabController.updateView();
    }

    public void refreshBorrowTab() {

        if (borrowTabController == null) {
            return;
        }
        System.out.println("Refresh borrow");
        borrowTabController.updateView();
    }

    public void refreshNotificationTab() {

        if (notificationTabController == null) {
            return;
        }
        System.out.println("Refresh notification");
        notificationTabController.updateView();
    }

    public void refreshAll() {
        refreshBookTab();
        refreshBorrowTab();
        refreshCustomerTab();
        refreshNotificationTab();
    }
}
