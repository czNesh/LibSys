/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import models.BookTableModel;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;
import services.BorrowService;
import views.BookBorrowDialog;
import views.DatePicker;

/**
 *
 * @author Administrator
 */
public class BookBorrowController extends BaseController {

    BookBorrowDialog dialog;
    Customer customer;
    List<Book> booksList = new ArrayList<>();

    public BookBorrowController(JFrame parent) {
        dialog = new BookBorrowDialog(parent, true);
        initListeners();
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

    private void initListeners() {
        BookBorrowActionListener a = new BookBorrowActionListener();
        dialog.getSearchCustomerButton().addActionListener(a);
        dialog.getDateFromButton().addActionListener(a);
        dialog.getDateToButton().addActionListener(a);
        dialog.getAddBookButton().addActionListener(a);
        dialog.getCancelButton().addActionListener(a);
        dialog.getConfirmButton().addActionListener(a);

    }

    private class BookBorrowActionListener implements ActionListener {

        public BookBorrowActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "searchCustomer":
                    CustomerListController clc = new CustomerListController(null, true);
                    clc.showView();
                    customer = clc.getSeletedCustomer();
                    if (customer != null) {
                        dialog.getInputCustomer().setText(customer.toString());
                    }
                    break;
                case "dateFrom":
                    DatePicker dateFrom = new DatePicker(null, true);
                    dateFrom.setLocationRelativeTo(null);
                    dateFrom.setVisible(true);

                    if (dateFrom.getDate() != null) {
                        Date d = dateFrom.getDate();
                        dialog.getInputDateFrom().setText(DateFormater.dateToString(d, false));
                    }

                    break;
                case "dateTo":
                    DatePicker dateTo = new DatePicker(null, true);
                    dateTo.setLocationRelativeTo(null);
                    dateTo.setVisible(true);

                    if (dateTo.getDate() != null) {
                        Date d = dateTo.getDate();
                        dialog.getInputDateTo().setText(DateFormater.dateToString(d, false));
                    }

                    break;
                case "addBook":
                    BookListController blc = new BookListController(null, true);
                    blc.showView();
                    if (blc.getBooks().isEmpty()) {
                        return;
                    }
                    for (Book b : blc.getBooks()) {
                        booksList.add(b);
                    }
                    dialog.getSelectedBooksTable().setModel(new BookTableModel(booksList));
                    break;

                case "confirm":
                    StringBuilder validationLog = new StringBuilder();

                    Date from = DateFormater.stringToDate(dialog.getInputDateFrom().getText(), false);
                    Date to = DateFormater.stringToDate(dialog.getInputDateTo().getText(), false);

                    if (booksList.isEmpty()) {
                        validationLog.append("Žádná položka k vypujčení\n");
                    }
                    if (from == null) {
                        validationLog.append("Datum (od) nemá správný tvar\n");
                    }
                    if (to == null) {
                        validationLog.append("Datum (do) nemá správný tvar\n");
                    }

                    if (customer == null) {
                        validationLog.append("Nebyl vybrán žádný zákazník\n");
                    }

                    if (validationLog.length() > 0) {
                        JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Borrow b = new Borrow();
                        b.setFromDate(from);
                        b.setToDate(to);
                        b.setReturned(Byte.valueOf("0"));
                        b.setCustomer(customer);
                        BorrowService.getInstance().newBorrow(b, booksList);
                        dispose();
                    }
                    break;

                case "cancel":
                    dispose();
                    break;
                default:
                    break;


            }
        }
    }
}
