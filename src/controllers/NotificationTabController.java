/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.DateHelper;
import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.NotificationTableModel;
import models.entity.Notification;
import views.DatePicker;
import views.MainView;
import views.NotificationFilterDialog;
import views.NotificationSearchDialog;

/**
 *
 * @author Nesh
 */
public class NotificationTabController {

    private MainView mainView;
    private NotificationTableModel tableModel;
    private NotificationSearchDialog nsd;
    private NotificationFilterDialog filter;
    Configuration c = Configuration.getInstance();

    public NotificationTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new NotificationTableModel();
        mainView.getTABnotification().setModel(tableModel);

        //Filter
        filter = new NotificationFilterDialog(mainView, true);
        setFilterData();

        // Notification Search Dialog
        nsd = new NotificationSearchDialog(mainView, true);

        // INIT LISTENERS
        initListeners();

        // update view
        updateView();
    }

    private void initListeners() {
        NotificationTabKeyListener k = new NotificationTabKeyListener();
        mainView.getINPnotificationPage().addKeyListener(k);
        mainView.getINPnotificationFilter().addKeyListener(k);

        NotificationTabActionListerner a = new NotificationTabActionListerner();
        mainView.getBTNnotificationFilter().addActionListener(a);
        mainView.getBTNnotificationNext().addActionListener(a);
        mainView.getBTNnotificationPrev().addActionListener(a);
        mainView.getBTNnotificationStopSearch().addActionListener(a);
        mainView.getBTNsearch().addActionListener(a);
        mainView.getQrcodeButton().addActionListener(a);
        mainView.getBarcodeButton().addActionListener(a);

        // FILTER
        filter.getBTNok().addActionListener(a);

        // SEARCH DIALOG
        nsd.getBTNsearch().addActionListener(a);
        nsd.getBTNreset().addActionListener(a);
        nsd.getBTNclose().addActionListener(a);
        nsd.getBTNfrom().addActionListener(a);
        nsd.getBTNto().addActionListener(a);

        NotificationTabMouseListener m = new NotificationTabMouseListener();
        mainView.getTABnotification().addMouseListener(m);
    }

    public void updateView() {
        // TableModel
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        mainView.getTabPanel().setTitleAt(3, "Oznámení (" + tableModel.getTotalNotificationsCount() + ")");

        // Update page counting 
        mainView.getINPnotificationPage().setText(String.valueOf(tableModel.getPage()));
        mainView.getINPnotificationTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            mainView.getBTNnotificationPrev().setEnabled(false);
        } else {
            mainView.getBTNnotificationPrev().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            mainView.getBTNnotificationNext().setEnabled(false);
        } else {
            mainView.getBTNnotificationNext().setEnabled(true);
        }
    }

    private void setFilterData() {
        filter.getINPborrowCode().setSelected(c.isNotificationShowBorrowCode());
        filter.getINPcustomer().setSelected(c.isNotificationShowCustomer());
        filter.getINPfrom().setSelected(c.isNotificationShowFrom());
        filter.getINPto().setSelected(c.isNotificationShowTo());
        filter.getINPtype().setSelected(c.isNotificationShowType());
        filter.getINPitem().setSelected(c.isNotificationsShowItem());
        filter.getINPmaxRows().setValue((int) c.getMaxNotificationRowsCount());

    }

    private class NotificationTabMouseListener implements MouseListener {

        public NotificationTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Notification temp = tableModel.getNotification(mainView.getTABnotification().getSelectedRow());
                NotificationDetailController ndc = new NotificationDetailController(temp);
                ndc.showView();
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

    private class NotificationTabActionListerner implements ActionListener {

        public NotificationTabActionListerner() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "filter":
                    filter.setLocationRelativeTo(null);
                    filter.setVisible(true);
                    break;

                case "filterConfirmed":

                    c.setNotificationShowBorrowCode(filter.getINPborrowCode().isSelected());
                    c.setNotificationShowCustomer(filter.getINPcustomer().isSelected());
                    c.setNotificationShowFrom(filter.getINPfrom().isSelected());
                    c.setNotificationShowTo(filter.getINPto().isSelected());
                    c.setNotificationShowType(filter.getINPtype().isSelected());
                    c.setNotificationsShowItem(filter.getINPitem().isSelected());


                    c.setMaxNotificationRowsCount((int) filter.getINPmaxRows().getValue());
                    updateView();
                    filter.setVisible(false);
                    break;
                case "searchDialog":
                    if (mainView.getTabPanel().getSelectedIndex() != 3) {
                        break;
                    }
                    nsd.setLocationRelativeTo(null);
                    nsd.setVisible(true);
                    break;

                case "close":
                    nsd.setVisible(false);
                    break;
                case "reset":
                    nsd.getINPborrowCode().setText("");
                    nsd.getINPcustomer().setText("");
                    nsd.getINPbook().setText("");
                    nsd.getINPfrom().setText("");
                    nsd.getINPto().setText("");
                    nsd.getINPtype().setSelectedIndex(0);
                    break;
                case "stop":
                    nsd.getINPborrowCode().setText("");
                    nsd.getINPcustomer().setText("");
                    nsd.getINPbook().setText("");
                    nsd.getINPfrom().setText("");
                    nsd.getINPto().setText("");
                    nsd.getINPtype().setSelectedIndex(0);
                    tableModel.stopSearch();
                    updateView();
                    mainView.getBTNnotificationStopSearch().setVisible(false);
                    break;
                case "fromDate":
                    DatePicker dp = new DatePicker(null, true);
                    dp.setLocationRelativeTo(null);
                    dp.setVisible(true);

                    if (dp.getDate() != null) {
                        Date d = dp.getDate();
                        nsd.getINPfrom().setText(DateHelper.dateToString(d, false));
                    }
                    break;
                case "toDate":
                    DatePicker dp2 = new DatePicker(null, true);
                    dp2.setLocationRelativeTo(null);
                    dp2.setVisible(true);

                    if (dp2.getDate() != null) {
                        Date d2 = dp2.getDate();
                        nsd.getINPto().setText(DateHelper.dateToString(d2, false));
                    }
                    break;
                case "search":
                    tableModel.search(
                            nsd.getINPborrowCode().getText(),
                            nsd.getINPcustomer().getText(),
                            nsd.getINPbook().getText(),
                            nsd.getINPfrom().getText(),
                            nsd.getINPto().getText(),
                            nsd.getINPtype().getSelectedIndex());
                    updateView();
                    nsd.setVisible(false);
                    mainView.getBTNnotificationStopSearch().setVisible(true);
                    break;
                case "nextPage":
                    tableModel.nextPage();
                    updateView();
                    break;
                case "prevPage":
                    tableModel.prevPage();
                    updateView();
                    break;
                case "barcode":
                    JOptionPane.showMessageDialog(mainView, "Oznámení nelze tisknout do BARCODE", "Informace", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "qrcode":
                    JOptionPane.showMessageDialog(mainView, "Oznámení nelze tisknout do QRCODE", "Informace", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    break;
            }

        }
    }

    private class NotificationTabKeyListener implements KeyListener {

        public NotificationTabKeyListener() {
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
                case "filter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getINPnotificationFilter().getText().trim().isEmpty()) {
                            tableModel.resetFilter();
                        } else {
                            tableModel.setFilter(mainView.getINPnotificationFilter().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "page":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getINPnotificationPage().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
            }
        }
    }
}
