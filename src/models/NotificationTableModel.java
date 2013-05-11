/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import helpers.DateHelper;
import io.Configuration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Borrow;
import models.entity.Notification;
import models.entity.NotificationType;
import services.BorrowService;

/**
 *
 * @author Nesh
 */
public class NotificationTableModel extends AbstractTableModel {

    private List<Notification> notifications;
    private List<Notification> exactNotifications;
    // viditelnost
    private boolean showType;
    private boolean showCustomer;
    private boolean showBorrowCode;
    private boolean showItems;
    private boolean showFrom;
    private boolean showTo;
    //nastaveni zobrazeni
    private int page = 1;
    private int maxRows;
    private String filterString = "";
    private boolean isSearching = false;

    public NotificationTableModel() {
        notifications = new ArrayList<>();
        exactNotifications = new ArrayList<>();
    }

    public void updateData() {
        Configuration c = Configuration.getInstance();
        maxRows = c.getMaxNotificationRowsCount();

        showType = c.isNotificationShowType();
        showCustomer = c.isNotificationShowCustomer();
        showBorrowCode = c.isNotificationShowBorrowCode();
        showItems = c.isNotificationsShowItem();
        showFrom = c.isNotificationShowFrom();
        showTo = c.isNotificationShowTo();

        if (!isSearching) {
            notifications.clear();
            List<Borrow> lateBorrows = BorrowService.getInstance().getLateBorrows();
            for (Borrow b : lateBorrows) {
                notifications.add(new Notification(NotificationType.NOT_RETURNED, b));
            }

            List<Borrow> longBorrows = BorrowService.getInstance().getLongBorrows();
            for (Borrow b : longBorrows) {
                notifications.add(new Notification(NotificationType.LONG_TIME_BORROW, b));
            }
        }

        if (!filterString.isEmpty()) {
            applyFilter();
        }

    }

    @Override
    public int getRowCount() {
        if (exactNotifications.isEmpty()) {
            if (notifications == null) {
                return 0;
            }
            if (notifications.size() >= maxRows * page) {
                return maxRows;
            } else {
                return notifications.size() % maxRows;
            }
        } else {
            if (exactNotifications == null) {
                return 0;
            }
            if (exactNotifications.size() >= maxRows * page) {
                return maxRows;
            } else {
                return exactNotifications.size() % maxRows;
            }
        }
    }

    @Override
    public int getColumnCount() {
        int i = 0;

        if (showType) {
            i++;
        }
        if (showCustomer) {
            i++;
        }
        if (showBorrowCode) {
            i++;
        }
        if (showItems) {
            i++;
        }
        if (showFrom) {
            i++;
        }
        if (showTo) {
            i++;
        }
        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Notification n;
        if (exactNotifications.isEmpty()) {
            n = notifications.get(rowIndex + moveByPage());
        } else {
            n = exactNotifications.get(rowIndex + moveByPage());
        }

        ArrayList<String> tempValues = new ArrayList<>();
        if (showType) {
            switch (n.getType()) {
                case NOT_RETURNED:
                    tempValues.add("Nevrácená kniha");
                    break;
                case LONG_TIME_BORROW:
                    tempValues.add("Dlouho vypůjčená kniha");
                    break;
            }
        }
        if (showCustomer) {
            tempValues.add(n.getBorrow().getCustomer().getFullName());
        }
        if (showBorrowCode) {
            tempValues.add(n.getBorrow().getBorrowCode());
        }
        if (showItems) {
            tempValues.add(n.getBorrow().getItem().getTitle());
        }
        if (showFrom) {
            tempValues.add(DateHelper.dateToString(n.getBorrow().getFromDate(), false));
        }
        if (showTo) {
            tempValues.add(DateHelper.dateToString(n.getBorrow().getToDate(), false));
        }
        return tempValues.get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();
        if (showType) {
            tempValuesColumnNames.add("Typ oznámení");
        }
        if (showCustomer) {
            tempValuesColumnNames.add("Zákazník");
        }
        if (showBorrowCode) {
            tempValuesColumnNames.add("Půjčka číslo");
        }
        if (showItems) {
            tempValuesColumnNames.add("Kniha");
        }
        if (showFrom) {
            tempValuesColumnNames.add("Od");
        }

        if (showTo) {
            tempValuesColumnNames.add("Do");
        }

        return tempValuesColumnNames.get(column);
    }

    public void applyFilter() {
        exactNotifications.clear();
        for (Notification n : notifications) {

            // kontrola dlouhodobe
            if (exactNotifications.equals("dlouhodobe") || filterString.equals("dlouhodobé")) {
                if (n.getType().equals(NotificationType.LONG_TIME_BORROW)) {
                    exactNotifications.add(n);
                    continue;
                }
            }
            // kontrola nevracene
            if (filterString.equals("nevracene") || filterString.equals("nevrácené")) {
                if (n.getType().equals(NotificationType.NOT_RETURNED)) {
                    exactNotifications.add(n);
                    continue;
                }
            }

            // kontrola kod pujcky
            if (n.getBorrow().getBorrowCode().equals(filterString)) {
                exactNotifications.add(n);
                continue;
            }

            //kontrola jmena zakaznika
            if (n.getBorrow().getCustomer().getFullName().toLowerCase().contains(filterString)) {
                exactNotifications.add(n);
                continue;
            }

            //kontrola podle knihy
            if (n.getBorrow().getItem().getTitle().toLowerCase().contains(filterString)) {
                exactNotifications.add(n);
                continue;
            }

            //kontrola podle datumu
            Date d = DateHelper.stringToDate(filterString, false);
            if (d != null) {
                if (DateHelper.compareEQ(d, n.getBorrow().getFromDate()) || DateHelper.compareEQ(d, n.getBorrow().getToDate())) {
                    exactNotifications.add(n);
                    continue;
                }
            }
        }
        setPage(1);
    }

    public void setFilter(String filter) {
        this.filterString = filter;
    }

    public void resetFilter() {
        exactNotifications.clear();
        this.filterString = "";
    }

    public void setPage(int page) {
        if (page <= getTotalPageCount() && page > 0) {
            this.page = page;
        }
    }

    public void nextPage() {
        if (page + 1 <= getTotalPageCount()) {
            setPage(page + 1);
        }
    }

    public void prevPage() {
        if (page - 1 > 0) {
            setPage(page - 1);
        }
    }

    public int getTotalPageCount() {
        if (exactNotifications.isEmpty()) {
            if (notifications.size() % maxRows > 0) {
                return notifications.size() / maxRows + 1;
            } else {
                return notifications.size() / maxRows;
            }
        } else {
            if (exactNotifications.size() % maxRows > 0) {
                return exactNotifications.size() / maxRows + 1;
            } else {
                return exactNotifications.size() / maxRows;
            }
        }
    }

    public int getPage() {
        return page;
    }

    public int getTotalNotificationsCount() {
        return BorrowService.getInstance().getLateBorrows().size() + BorrowService.getInstance().getLongBorrows().size();
    }

    private int moveByPage() {
        return ((page - 1) * maxRows);
    }

    public void stopSearch() {
        this.isSearching = false;
        filterString = "";
    }

    public void search(String borrowCode, String customer, String book, String from, String to, int type) {
        isSearching = true;
        borrowCode = borrowCode.toLowerCase().trim();
        customer = customer.toLowerCase().trim();
        book = book.toLowerCase().trim();
        from = from.toLowerCase().trim();
        to = to.toLowerCase().trim();

        List<Notification> list = new ArrayList<>(notifications);

        notifications.clear();

        for (Notification n : list) {

            // kontrola dlouhodobe
            if (type != 0) {
                if (type == 2) {
                    if (!n.getType().equals(NotificationType.LONG_TIME_BORROW)) {
                        continue;
                    }
                }
                // kontrola nevracene
                if (type == 1) {
                    if (!n.getType().equals(NotificationType.NOT_RETURNED)) {
                        continue;
                    }
                }
            }

            // kontrola kod pujcky
            if (!borrowCode.isEmpty()) {
                if (!n.getBorrow().getBorrowCode().equals(borrowCode)) {
                    continue;
                }
            }
            //kontrola jmena zakaznika
            if (!customer.isEmpty()) {
                if (!n.getBorrow().getCustomer().getFullName().toLowerCase().contains(customer)) {
                    continue;
                }
            }
            //kontrola podle knihy
            if (!book.isEmpty()) {
                if (!n.getBorrow().getItem().getTitle().toLowerCase().contains(book)) {
                    continue;
                }
            }
            //kontrola podle datum

            if (!from.isEmpty()) {

                Date d = DateHelper.stringToDate(from, false);

                if (d == null || !DateHelper.compareEQ(d, n.getBorrow().getFromDate())) {
                    continue;
                }
            }
            System.out.println("2");
            if (!to.isEmpty()) {
                Date d2 = DateHelper.stringToDate(to, false);

                if (d2 == null || !DateHelper.compareEQ(d2, n.getBorrow().getToDate())) {
                    continue;
                }
            }
            System.out.println("3");
            notifications.add(n);
        }
        setPage(1);
    }

    public Notification getNotification(int selectedRow) {
        if (exactNotifications.isEmpty()) {
            return notifications.get(moveByPage() + selectedRow);
        } else {
            return exactNotifications.get(moveByPage() + selectedRow);
        }
    }
}
