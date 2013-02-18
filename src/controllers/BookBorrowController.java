/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateFormater;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import models.entity.Book;
import models.entity.Customer;
import views.BookBorrowDialog;
import views.DatePicker;

/**
 *
 * @author Administrator
 */
public class BookBorrowController extends BaseController {

    BookBorrowDialog dialog;
    Customer customer;
    List<Book> booksList;

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
                    dialog.getInputCustomer().setText(customer.toString());
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
                    booksList = blc.getBooks();
                    
                    break;
                default:
                    break;


            }
        }
    }
}
