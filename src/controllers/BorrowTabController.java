/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.BorrowTableModel;
import models.entity.Borrow;
import services.BorrowService;
import views.BorrowFilterDialog;
import views.BorrowSearchDialog;
import views.MainView;

/**
 *
 * @author Nesh
 */
public class BorrowTabController {

    MainView mainView;
    BorrowTableModel tableModel;
    BorrowFilterDialog filter;
    Map<String, String> orderType = BorrowService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = BorrowService.getInstance().getOrderByMap();
    BorrowSearchDialog bsd;

    public BorrowTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new BorrowTableModel();
        mainView.getBorrowTable().setModel(tableModel);

        // FilterDialog
        filter = new BorrowFilterDialog(mainView, true);
        setFilterData();

        // Borrow Search Dialog
        bsd = new BorrowSearchDialog(mainView, true);

        // InitListeners
        initListeners();

        // UpdateView
        updateView();
    }

    private void initListeners() {
        // MouseListeners
        BorrowTabMouseListener m = new BorrowTabMouseListener();
        mainView.getBorrowTable().addMouseListener(m);

        // ActionListener
        BorrowTabActionListener a = new BorrowTabActionListener();
        mainView.getBorrowNextButton().addActionListener(a);
        mainView.getBorrowPrevButton().addActionListener(a);
        mainView.getBorrowFilterButton().addActionListener(a);
        mainView.getBTNsearch().addActionListener(a);
        mainView.getBTNstopBorrowSearch().addActionListener(a);

        // KeyListener
        BorrowTabKeyListener k = new BorrowTabKeyListener();
        mainView.getINPborrowFilter().addKeyListener(k);
        mainView.getINPborrowPageNumber().addKeyListener(k);
        mainView.getBorrowTable().addKeyListener(k);

        // FILTER Listeners
        filter.getBTNok().addActionListener(a);

        //SEARCH listener
        bsd.getBTNsearch().addActionListener(a);
        bsd.getBTNcloseSearchDialog().addActionListener(a);
        bsd.getBTNreset().addActionListener(a);
    }

    public void updateView() {
        // UPDATE DATA
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getINPborrowPageNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBorrowTotalPageNumber().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            mainView.getBorrowPrevButton().setEnabled(false);
        } else {
            mainView.getBorrowPrevButton().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            mainView.getBorrowNextButton().setEnabled(false);
        } else {
            mainView.getBorrowNextButton().setEnabled(true);
        }
    }

    private void setFilterData() {
        filter.getINPcustomer().setSelected(Configuration.getInstance().isBorrowShowCustomer());
        filter.getINPitems().setSelected(Configuration.getInstance().isBorrowShowItems());
        filter.getINPfrom().setSelected(Configuration.getInstance().isBorrowShowFrom());
        filter.getINPto().setSelected(Configuration.getInstance().isBorrowShowTo());
        filter.getINPreturned().setSelected(Configuration.getInstance().isBorrowShowReturned());
        filter.getINPlibrarian().setSelected(Configuration.getInstance().isBorrowShowLibrarian());

        filter.getINPborrowMaxRowsCount().setValue((int) Configuration.getInstance().getMaxBorrowRowsCount());

        for (Map.Entry<String, String> entry : orderType.entrySet()) {
            filter.getINPorderType().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBorrowOrderType())) {
                filter.getINPorderType().setSelectedItem(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : orderBy.entrySet()) {
            filter.getINPorderBy().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBorrowOrderBy())) {
                filter.getINPorderBy().setSelectedItem(entry.getKey());
            }
        }

        updateView();
    }

    private class BorrowTabMouseListener implements MouseListener {

        public BorrowTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(mainView.getBorrowTable().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
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
                    break;
                case "searchDialog":
                    if (mainView.getTabPanel().getSelectedIndex() != 2) {
                        break;
                    }
                    bsd.setLocationRelativeTo(null);
                    bsd.setVisible(true);
                    break;

                case "closeSearchDialog":
                    bsd.setVisible(false);
                    break;
                case "resetSearchDialog":
                    bsd.getINPborrowCode().setText("");
                    bsd.getINPcustomer().setText("");
                    bsd.getINPlibrarian().setText("");
                    bsd.getINPfrom().setText("");
                    bsd.getINPto().setText("");
                    bsd.getINPitem().setText("");
                    bsd.getINPstate().setSelectedIndex(0);
                    break;

                case "stopSearch":
                    bsd.getINPborrowCode().setText("");
                    bsd.getINPcustomer().setText("");
                    bsd.getINPlibrarian().setText("");
                    bsd.getINPfrom().setText("");
                    bsd.getINPto().setText("");
                    bsd.getINPitem().setText("");
                    bsd.getINPstate().setSelectedIndex(0);
                    tableModel.stopSearch();
                    updateView();
                    mainView.getBTNstopBorrowSearch().setVisible(false);
                    break;

                case "search":
                    tableModel.search(
                            bsd.getINPborrowCode().getText().trim(),
                            bsd.getINPcustomer().getText().trim(),
                            bsd.getINPlibrarian().getText().trim(),
                            bsd.getINPfrom().getText().trim(),
                            bsd.getINPto().getText().trim(),
                            bsd.getINPitem().getText().trim(),
                            bsd.getINPstate().getSelectedIndex());
                    updateView();
                    bsd.setVisible(false);

                    mainView.getBTNstopBorrowSearch().setVisible(true);

                    break;
                case "filterConfirmed":
                    Configuration.getInstance().setBorrowShowCustomer(filter.getINPcustomer().isSelected());
                    Configuration.getInstance().setBorrowShowItems(filter.getINPitems().isSelected());
                    Configuration.getInstance().setBorrowShowFrom(filter.getINPfrom().isSelected());
                    Configuration.getInstance().setBorrowShowTo(filter.getINPto().isSelected());
                    Configuration.getInstance().setBorrowShowReturned(filter.getINPreturned().isSelected());
                    Configuration.getInstance().setBorrowShowLibrarian(filter.getINPlibrarian().isSelected());

                    Configuration.getInstance().setBorrowOrderBy(orderBy.get((String) filter.getINPorderBy().getSelectedItem()));
                    Configuration.getInstance().setBorrowOrderType(orderType.get((String) filter.getINPorderType().getSelectedItem()));
                    Configuration.getInstance().setMaxBorrowRowsCount((int) filter.getINPborrowMaxRowsCount().getValue());
                    updateView();
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
            switch (((JComponent) e.getSource()).getName()) {
                case "borrowFilter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getINPborrowFilter().getText().trim().isEmpty()) {
                            tableModel.applyFilter("");
                        } else {
                            tableModel.applyFilter(mainView.getINPborrowFilter().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "borrowPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getINPborrowPageNumber().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
                case "borrowTable":

                    if (mainView.getBorrowTable().getSelectedRows().length == 0) {
                        return;
                    }
                    int isSure = JOptionPane.showInternalConfirmDialog(mainView.getContentPane(), "Opravdu chcete smazat záznamy o půjčce?", "Opravdu smazat?", JOptionPane.OK_CANCEL_OPTION);
                    if (isSure == JOptionPane.OK_OPTION) {
                        for (Borrow b : tableModel.getBorrows(mainView.getBorrowTable().getSelectedRows())) {
                            if (BorrowService.getInstance().isAllReturned(b.getBorrowCode())) {
                                BorrowService.getInstance().delete(b);
                            } else {
                                int isSure2 = JOptionPane.showInternalConfirmDialog(mainView.getContentPane(), "Výpůjčka je aktivní, opravdu smazat?", "Opravdu smazat", JOptionPane.ERROR_MESSAGE);
                                if (isSure2 == JOptionPane.OK_OPTION) {
                                    BorrowService.getInstance().delete(b);
                                }
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
