/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JTable;
import models.BorrowTableModel;
import models.entity.Borrow;
import views.BorrowFilterDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
class BorrowTabController {

    BorrowTableModel tableModel;
    BorrowFilterDialog filter;
    MainView mainView;

    public BorrowTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new BorrowTableModel();
        mainView.getBorrowTable().setModel(tableModel);

        // FilterDialog
        filter = new BorrowFilterDialog(mainView, true);

        // InitListeners
        initListeners();

        // UpdateView
        updateView();
    }

    private void initListeners() {
        // MouseListeners
        mainView.getBorrowTable().addMouseListener(new BorrowTabMouseListener());

        // ActionListener
        BorrowTabActionListener a = new BorrowTabActionListener();
        mainView.getBorrowNextButton().addActionListener(a);
        mainView.getBorrowPrevButton().addActionListener(a);
        mainView.getBorrowFilterButton().addActionListener(a);

        // KeyListener
        mainView.getBookTableInputNumber().addKeyListener(new BorrowTabKeyListener());

        // FILTER Listeners
        filter.getOkButton().addActionListener(a);
    }

    private void updateView() {
        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getBorrowInputPageNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBorrowTotalPageNumber().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

    }

    private class BorrowTabMouseListener implements MouseListener {

        public BorrowTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTable t = mainView.getCatalogTable();
                Borrow b = (Borrow) tableModel.getBorrow(t.getSelectedRow());
                //   BorrowDetailController bdc = new BorrowDetailController(b);
                //   bdc.showView();
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

    private class BorrowTabActionListener implements ActionListener {

        public BorrowTabActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "prevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
                case "filter":
                    filter.setLocationRelativeTo(null);
                    filter.setVisible(true);
                    tableModel.setViewSettings(
                            filter.getCustomerCheckBox().isSelected(),
                            filter.getItemCheckBox().isSelected(),
                            filter.getLibrarianCheckBox().isSelected(),
                            filter.getFromCheckBox().isSelected(),
                            filter.getToCheckBox().isSelected(),
                            filter.getReturnedCheckBox().isSelected());
                    updateView();
                    filter.setVisible(false);
                    break;

                case "filterConfirm":
                    filter.setVisible(false);
                    break;
                default:
                    break;
            }
        }
    }

    private class BorrowTabKeyListener implements KeyListener {

        public BorrowTabKeyListener() {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String in = mainView.getBookTableInputNumber().getText();
                try {
                    tableModel.setPage(Integer.parseInt(in));
                } catch (NumberFormatException ex) {
                    System.out.println("NESPRAVNY FORMAT CISLA");
                }
                updateView();
            }
        }
    }
}
