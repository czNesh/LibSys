/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import coding.Barcode;
import coding.QRCode;
import helpers.DateHelper;
import io.FileManager;
import io.PDFPrinter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.BookTableModel;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Customer;
import services.BorrowService;
import services.CustomerService;
import views.BorrowDetailDialog;
import views.BorrowSplitDialog;
import views.DatePicker;

/**
 *
 * @author Nesh
 */
public class BorrowDetailController extends BaseController {

    private BorrowDetailDialog dialog;
    private Borrow borrow;
    private boolean editMode;
    private BookTableModel tableModel;
    private List<Borrow> connectedBorrows;
    private Long borrowId;
    private BorrowSplitDialog splitDialog;

    public BorrowDetailController(Borrow borrow) {
        dialog = new BorrowDetailDialog(null, true);
        this.borrow = borrow;
        this.borrowId = borrow.getId();
        editMode = false;
        showData();
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

    private void showData() {
        borrow = BorrowService.getInstance().find(borrowId);
        connectedBorrows = BorrowService.getInstance().getBorrows(borrow.getBorrowCode());

        // Knihy
        List<Book> books = BorrowService.getInstance().getBooksOfBorrow(borrow.getBorrowCode());
        tableModel = new BookTableModel(books, borrow.getBorrowCode());
        dialog.getTABbooks().setModel(tableModel);


        // PUJCKA
        dialog.getINPborrowCode().setText(borrow.getBorrowCode());
        dialog.getINPlibrarian().setText(borrow.getLibrarian().toString());
        dialog.getINPfrom().setText(DateHelper.dateToString(borrow.getFromDate(), false));
        dialog.getINPto().setText(DateHelper.dateToString(borrow.getToDate(), false));
        int returned = BorrowService.getInstance().getReturned(borrow.getBorrowCode());
        int totalCount = BorrowService.getInstance().getCount(borrow.getBorrowCode());

        if (returned == totalCount) {
            dialog.getINPstate().setForeground(new Color(0, 163, 48));
        } else {
            dialog.getINPstate().setForeground(Color.RED);
        }

        dialog.getINPstate().setText("Vráceno " + returned + " / " + totalCount + " položek");
        dialog.getINPnotes().setText(borrow.getNotes());

        // ZAKAZNIK
        dialog.getINPcustomerSsn().setText(borrow.getCustomer().getStringSSN());
        dialog.getINPcustomerName().setText(borrow.getCustomer().getFullName());
        dialog.getINPcustomerAdress().setText(borrow.getCustomer().getFullAdress());
        dialog.getINPcustomerPhone().setText(borrow.getCustomer().getPhone());
        dialog.getINPcutomerEmail().setText(borrow.getCustomer().getEmail());
        dialog.getINPcustomerNotes().setText(borrow.getCustomer().getNotes());
    }

    private void initListeners() {
        BorrowDialogActionListener a = new BorrowDialogActionListener();
        dialog.getBTNedit().addActionListener(a);
        dialog.getBTNclose().addActionListener(a);
        dialog.getBTNprint().addActionListener(a);
        dialog.getBTNqrcode().addActionListener(a);
        dialog.getBTNbarcode().addActionListener(a);
        dialog.getBTNcompleteBorrow().addActionListener(a);
        dialog.getBTNeditSelectedBooksTerm().addActionListener(a);
        dialog.getBTNreturnSelectedBooks().addActionListener(a);
        dialog.getBTNfrom().addActionListener(a);
        dialog.getBTNto().addActionListener(a);
        dialog.getBTNnotReturned().addActionListener(a);

        BorrowDetailMouseListener m = new BorrowDetailMouseListener();
        dialog.getTABbooks().addMouseListener(m);
    }

    private class BorrowDetailMouseListener implements MouseListener {

        public BorrowDetailMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
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

    private class BorrowDialogActionListener implements ActionListener {

        public BorrowDialogActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (((JComponent) e.getSource()).getName()) {
                case "edit":
                    switchEditMode();
                    break;
                case "save":
                    saveBorrow();
                    break;
                case "close":
                    dialog.dispose();
                    dialog = null;
                    break;
                case "printPDF":
                    PDFPrinter.getInstance().printBorrow(borrow);
                    break;
                case "qrcode":
                    BufferedImage qrcode = QRCode.encode(borrow.getBorrowCode());
                    FileManager.getInstance().saveImage("qr_borrow_" + borrow.getBorrowCode(), qrcode);
                    FileManager.getInstance().openImage("qr_borrow_" + borrow.getBorrowCode());
                    break;
                case "barcode":
                    BufferedImage barcode = Barcode.encode(borrow.getBorrowCode());
                    FileManager.getInstance().saveImage("ba_borrow_" + borrow.getBorrowCode(), barcode);
                    FileManager.getInstance().openImage("ba_borrow_" + borrow.getBorrowCode());
                    break;
                case "dateFrom":
                    DatePicker dp = new DatePicker(null, true);
                    dp.setLocationRelativeTo(null);
                    dp.setVisible(true);

                    if (dp.getDate() != null) {
                        Date d = dp.getDate();
                        dialog.getINPfrom().setText(DateHelper.dateToString(d, false));
                    }
                    break;
                case "dateTo":
                    DatePicker dp2 = new DatePicker(null, true);
                    dp2.setLocationRelativeTo(null);
                    dp2.setVisible(true);

                    if (dp2.getDate() != null) {
                        Date d2 = dp2.getDate();
                        dialog.getINPto().setText(DateHelper.dateToString(d2, false));
                    }
                    break;
                case "completeBorrow":
                    completeBorrow();
                    break;
                case "return":
                    returnSelected();
                    break;
                case "notReturn":
                    notReturnSelected();
                    break;
                case "editTerm":
                    editTerm();
                    break;
                default:
                    break;
            }
        }

        private void switchEditMode() {
            if (editMode) {
                dialog.getBTNedit().setName("edit");
                dialog.getBTNedit().setText("Upravit");
                dialog.getINPcustomerNotes().setBackground(new Color(240, 240, 240));
                dialog.getINPnotes().setBackground(new Color(240, 240, 240));
            } else {
                dialog.getBTNedit().setName("save");
                dialog.getBTNedit().setText("Uložit");
                dialog.getINPcustomerNotes().setBackground(Color.white);
                dialog.getINPnotes().setBackground(Color.white);
            }

            dialog.getINPnotes().setEditable(!editMode);
            dialog.getINPcustomerNotes().setEditable(!editMode);
            dialog.getBTNfrom().setVisible(!editMode);
            dialog.getBTNto().setVisible(!editMode);
            editMode = !editMode;

        }

        private void saveBorrow() {

            StringBuilder validationLog = new StringBuilder();
            Date from = DateHelper.stringToDate(dialog.getINPfrom().getText(), false);
            Date to = DateHelper.stringToDate(dialog.getINPto().getText(), false);

            if (from == null) {
                validationLog.append("Datum (od) nemá správný tvar\n");
            }
            if (to == null) {
                validationLog.append("Datum (do) nemá správný tvar\n");
            }

            if (!DateHelper.compareGE(to, from)) {
                validationLog.append("Vypůjčka musí být minimálně na jeden den\n");
            }
            if (validationLog.length() > 0) {
                JOptionPane.showMessageDialog(dialog, validationLog.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
            } else {
                for (Borrow b : connectedBorrows) {
                    Customer c = b.getCustomer();
                    c.setNotes(dialog.getINPcustomerNotes().getText());
                    b.setNotes(dialog.getINPnotes().getText());
                    b.setFromDate(from);
                    b.setToDate(to);
                    CustomerService.getInstance().save(c);
                    BorrowService.getInstance().save(b);
                    switchEditMode();
                }
            }
            showData();
        }

        private void completeBorrow() {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Všechny knihy budou označeny jako vrácené?", "Opravdu dokončit?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                BorrowService.getInstance().returnBorrows(connectedBorrows);
                showData();
                RefreshController.getInstance().refreshBorrowTab();
            }
        }

        private void returnSelected() {
            if (dialog.getTABbooks().getSelectedRows().length > 0) {
                int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Označit vybrané knihy jako vrácené?", "Opravdu vrátit?", JOptionPane.OK_CANCEL_OPTION);
                if (isSure == JOptionPane.OK_OPTION) {
                    List<Book> tempList = tableModel.getBooks(dialog.getTABbooks().getSelectedRows());
                    BorrowService.getInstance().returnBorrows(tempList, borrow.getBorrowCode());
                    showData();
                    RefreshController.getInstance().refreshBorrowTab();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void notReturnSelected() {
            if (dialog.getTABbooks().getSelectedRows().length > 0) {
                int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Označit vybrané knihy jako nevrácené?", "Opravdu nevrácené?", JOptionPane.OK_CANCEL_OPTION);
                if (isSure == JOptionPane.OK_OPTION) {
                    List<Book> tempList = tableModel.getBooks(dialog.getTABbooks().getSelectedRows());
                    BorrowService.getInstance().notReturnBorrows(tempList, borrow.getBorrowCode());
                    showData();
                    RefreshController.getInstance().refreshBorrowTab();
                    RefreshController.getInstance().refreshNotificationTab();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void editTerm() {
            if (dialog.getTABbooks().getSelectedRows().length > 0) {
                int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Změnou data u položek dojde k rozdělení půjček", "Pokračovat?", JOptionPane.OK_CANCEL_OPTION);
                if (isSure == JOptionPane.OK_OPTION) {
                    BorrowSplitController bsc = new BorrowSplitController();
                    bsc.setFrom(borrow.getFromDate());
                    bsc.setTo(borrow.getToDate());
                    bsc.showView();

                    if (!bsc.isDatesSet()) {
                        return;
                    }

                    Date from = bsc.getFrom();
                    Date to = bsc.getTo();
                    assert (from != null || to != null);

                    if (DateHelper.compareEQ(from, borrow.getFromDate()) && DateHelper.compareEQ(to, borrow.getToDate())) {
                        JOptionPane.showMessageDialog(dialog, "Nebude provedena žádná akce", "Data se shodují", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    List<Book> tempBook = tableModel.getBooks(dialog.getTABbooks().getSelectedRows());
                    List<Borrow> tempBorrow = BorrowService.getInstance().getBorrows(tempBook, borrow.getBorrowCode());


                    if (!tempBorrow.isEmpty()) {
                        BorrowService.getInstance().splitBorrows(from, to, tempBorrow);
                    }
                    showData();
                    RefreshController.getInstance().refreshBorrowTab();

                    BorrowDetailController detail = new BorrowDetailController(tempBorrow.get(0));
                    detail.showView();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
