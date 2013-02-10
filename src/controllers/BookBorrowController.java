/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.swing.JFrame;
import views.BookBorrowDialog;

/**
 *
 * @author Administrator
 */
public class BookBorrowController extends BaseController {

    BookBorrowDialog dialog;

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
    }
}
