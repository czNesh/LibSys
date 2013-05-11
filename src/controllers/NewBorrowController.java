/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateHelper;
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
import views.DatePicker;
import views.NewBorrowDialog;

/**
 *
 * @author Administrator
 */
public class NewBorrowController extends BaseController {

    NewBorrowDialog dialog;
    Customer customer;
    List<Book> booksList = new ArrayList<>();

    public NewBorrowController(JFrame parent) {
        dialog = new NewBorrowDialog(parent, true);
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
        dialog.getBTNremove().addActionListener(a);
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
                        dialog.getInputDateFrom().setText(DateHelper.dateToString(d, false));
                    }

                    break;
                case "dateTo":
                    DatePicker dateTo = new DatePicker(null, true);
                    dateTo.setLocationRelativeTo(null);
                    dateTo.setVisible(true);

                    if (dateTo.getDate() != null) {
                        Date d = dateTo.getDate();
                        dialog.getInputDateTo().setText(DateHelper.dateToString(d, false));
                    }

                    break;
                case "addBook":
                    BookListController blc = new BookListController(null, true);
                    blc.showView();
                    if (blc.getBooks().isEmpty()) {
                        return;
                    }
                    for (Book b : blc.getBooks()) {
                        if (booksList.contains(b)) {
                            JOptionPane.showMessageDialog(dialog, "Kniha " + b.getTitle() + " (" + b.getBarcode() + ") je již v seznamu", "Kniha nebude přidána", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        booksList.add(b);
                    }
                    dialog.getSelectedBooksTable().setModel(new BookTableModel(booksList));
                    break;
                case "removeBook":
                    int[] indexes = dialog.getSelectedBooksTable().getSelectedRows();
                    for (int i = indexes.length - 1; i >= 0; i--) {
                        booksList.remove(indexes[i]);
                    }
                    dialog.getSelectedBooksTable().setModel(new BookTableModel(booksList));
                    break;
                case "confirm":
                    StringBuilder validationLog = new StringBuilder();

                    Date from = DateHelper.stringToDate(dialog.getInputDateFrom().getText(), false);
                    Date to = DateHelper.stringToDate(dialog.getInputDateTo().getText(), false);

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

                    if (!DateHelper.compareGE(to, from)) {
                        validationLog.append("Vypůjčka musí být minimálně na jeden den\n");
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
