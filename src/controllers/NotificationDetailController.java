/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateHelper;
import io.PDFPrinter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JComponent;
import models.BookTableModel;
import models.entity.Book;
import models.entity.Notification;
import services.BorrowService;
import views.NotificationDetailDialog;

/**
 *
 * @author Nesh
 */
public class NotificationDetailController extends BaseController {

    private Notification notification;
    private NotificationDetailDialog dialog;
    private BookTableModel tableModel;

    public NotificationDetailController(Notification notification) {
        // Notification
        this.notification = notification;

        //Dialog
        dialog = new NotificationDetailDialog(null, true);

        //Data
        updateData();

        //Listeners
        initListeners();
    }

    private void initListeners() {
        NotificationDialogActionListener a = new NotificationDialogActionListener();
        dialog.getBTNprint().addActionListener(a);
        dialog.getBTNclose().addActionListener(a);
        dialog.getBTNtoBorrow().addActionListener(a);

        NotificationDialogMouseListerner m = new NotificationDialogMouseListerner();
        dialog.getTABbooks().addMouseListener(m);
    }

    private void updateData() {
        // Knihy
        List<Book> books = BorrowService.getInstance().getBooksOfBorrow(notification.getBorrow().getBorrowCode());
        tableModel = new BookTableModel(books, notification.getBorrow().getBorrowCode());
        dialog.getTABbooks().setModel(tableModel);

        tableModel.fireTableDataChanged();

        dialog.getINPborrowCode().setText(notification.getBorrow().getBorrowCode());
        dialog.getINPcustomer().setText(notification.getBorrow().getCustomer().getFullName());
        dialog.getINPfrom().setText(DateHelper.dateToString(notification.getBorrow().getFromDate(), false));
        dialog.getINPto().setText(DateHelper.dateToString(notification.getBorrow().getToDate(), false));
        switch (notification.getType()) {
            case NOT_RETURNED:
                dialog.getINPnotificationType().setText("Nevrácená kniha");
                break;
            case LONG_TIME_BORROW:
                dialog.getINPnotificationType().setText("Dlouhodobě vypůjčená kniha");
                break;
        }

        int returned = BorrowService.getInstance().getReturned(notification.getBorrow().getBorrowCode());
        int totalCount = BorrowService.getInstance().getCount(notification.getBorrow().getBorrowCode());

        if (returned == totalCount) {
            dialog.getINPstate().setForeground(new Color(0, 163, 48));
        } else {
            dialog.getINPstate().setForeground(Color.RED);
        }

        dialog.getINPstate().setText("Vráceno " + returned + " / " + totalCount + " položek");
    }

    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    private class NotificationDialogMouseListerner implements MouseListener {

        public NotificationDialogMouseListerner() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (dialog.getTABbooks().getSelectedRowCount() > 0) {
                    dispose();
                    BorrowDetailController bdc = new BorrowDetailController(notification.getBorrow());
                    bdc.showView();
                }
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

    private class NotificationDialogActionListener implements ActionListener {

        public NotificationDialogActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "toBorrow":
                    dispose();
                    BorrowDetailController bdc = new BorrowDetailController(notification.getBorrow());
                    bdc.showView();
                    break;
                case "close":
                    dispose();
                    break;
                case "print":
                    PDFPrinter.getInstance().printNotification(notification);
                    break;

            }
        }
    }
}
