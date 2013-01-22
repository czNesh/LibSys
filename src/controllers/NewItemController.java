/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.entity.Book;
import services.GoogleBooksSearch;
import views.NewItemDialog;

/**
 *
 * @author Administrator
 */
public class NewItemController extends BaseController {

    private NewItemDialog dialog;
    GoogleBooksSearch gbs;

    public NewItemController(JFrame parent) {
        gbs = new GoogleBooksSearch();
        dialog = new NewItemDialog(parent, false);
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
        dialog.getSearchButton().addActionListener(new SearchButtonListener());
    }

    private class SearchButtonListener implements ActionListener {

        public SearchButtonListener() {
            gbs = new GoogleBooksSearch();
            gbs.addChangeListener(new SearchChangeListener());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gbs.setAutor("pecinovsky");
            gbs.search();
            Book temp = gbs.getBestResult();
            System.out.println(temp.getTitle());

        }
    }

    private class SearchChangeListener implements ChangeListener {

        public SearchChangeListener() {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            dialog.getSearchProgressBar().setValue(gbs.getStatus());
            dialog.getSearchProgressBar().setToolTipText(gbs.getTextStatus());
        }
    }
}
