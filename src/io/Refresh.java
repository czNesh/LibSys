/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import controllers.BookTabController;
import controllers.BorrowTabController;
import controllers.CustomerTabController;
import controllers.NotificationTabController;

/**
 *
 * @author petr.hejhal
 */
public class Refresh {

    public static Refresh instance;
    private BookTabController bookTabController;
    private CustomerTabController customerTabController;
    private BorrowTabController borrowTabController;
    private NotificationTabController notificationTabController;

    public static Refresh getInstance() {
        synchronized (Refresh.class) {
            if (instance == null) {
                instance = new Refresh();
            }
        }
        return instance;
    }

    private Refresh() {
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
        bookTabController.updateView();
    }

    public void refreshCustomerTab() {
        if (customerTabController == null) {
            return;
        }
        customerTabController.updateView();
    }

    public void refreshBorrowTab() {
        if (borrowTabController == null) {
            return;
        }
        borrowTabController.updateView();
    }

    public void refreshNotificationTab() {
        if (notificationTabController == null) {
            return;
        }
        notificationTabController.updateView();
    }
}
