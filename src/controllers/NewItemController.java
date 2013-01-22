/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.CatalogTableModel;
import models.entity.Book;
import models.entity.CatalogItem;
import services.GoogleBooksSearch;
import views.NewItemDialog;
import views.SearchResultsDialog;

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
            gbs.setAutor(dialog.getInputAuthor().getText());
            gbs.setTitle(dialog.getInputTitle().getText());
            gbs.setISBN(dialog.getInputISBN().getText());
            gbs.search();
            if (gbs.getResultsCount() == 0) {
                System.out.println("NOT MATCHES");
            } else {
                SearchResultsDialog resOut = new SearchResultsDialog(null, true);
                ArrayList<CatalogItem> temp = new ArrayList<>();
                ArrayList<Book> r = gbs.getResults();
                
                for (int i = 0; i < r.size(); i++) {
                    temp.add((CatalogItem) r.get(i));                   
                }

                CatalogTableModel ctm = new CatalogTableModel(temp);
                resOut.getResultsTable().setModel(ctm);
                
                resOut.setLocationRelativeTo(null);
                resOut.setVisible(true);
            }
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
