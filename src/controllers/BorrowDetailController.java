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
 * Třída (controller) starající se o detailní pohled na půjčku
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class BorrowDetailController extends BaseController {

    private BorrowDetailDialog dialog; // připojený pohled
    private Borrow borrow; // půjčka
    private boolean editMode; // mód editace
    private BookTableModel tableModel; // table model knih
    private List<Borrow> connectedBorrows; // spojene pujcky (podle borrowCode)
    private Long borrowId; // id pujcky
    private BorrowSplitDialog splitDialog; // dialog rozdeleni pujcek

    /**
     * Tridni konstruktor
     *
     * @param borrow aktuální půjčka
     */
    public BorrowDetailController(Borrow borrow) {
        dialog = new BorrowDetailDialog(null, true);
        this.borrow = borrow;
        this.borrowId = borrow.getId();
        editMode = false;
        updateView();
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    public void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        dialog.dispose();
        dialog = null;
    }

    /**
     * Inicializace listenerů
     */
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

    /**
     * Update dat v pohledu
     */
    private void updateView() {
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

    /**
     * Přepnutí pohledu z prohlížení do editace a zpět
     */
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

    /**
     * Ukládá změny v půjčce
     */
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
        updateView();
    }

    /**
     * Dokončuje celou půjčku (vrátí všechny knihy)
     */
    private void completeBorrow() {
        int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Všechny knihy budou označeny jako vrácené?", "Opravdu dokončit?", JOptionPane.OK_CANCEL_OPTION);
        if (isSure == JOptionPane.OK_OPTION) {
            BorrowService.getInstance().returnBorrows(connectedBorrows);
            updateView();
            RefreshController.getInstance().refreshBorrowTab();
        }
    }

    /**
     * Vrátí označené knihy
     */
    private void returnSelected() {
        if (dialog.getTABbooks().getSelectedRows().length > 0) {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Označit vybrané knihy jako vrácené?", "Opravdu vrátit?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                List<Book> tempList = tableModel.getBooks(dialog.getTABbooks().getSelectedRows());
                BorrowService.getInstance().returnBorrows(tempList, borrow.getBorrowCode());
                updateView();
                RefreshController.getInstance().refreshBorrowTab();
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Označí vybrané knihy jako nevrácené (pro opravu)
     */
    private void notReturnSelected() {
        if (dialog.getTABbooks().getSelectedRows().length > 0) {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Označit vybrané knihy jako nevrácené?", "Opravdu nevrácené?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                List<Book> tempList = tableModel.getBooks(dialog.getTABbooks().getSelectedRows());
                BorrowService.getInstance().notReturnBorrows(tempList, borrow.getBorrowCode());
                updateView();
                RefreshController.getInstance().refreshBorrowTab();
                RefreshController.getInstance().refreshNotificationTab();
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Změní termín u vybraných knih (rozdělení půjček)
     */
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
                updateView();
                RefreshController.getInstance().refreshBorrowTab();

                BorrowDetailController detail = new BorrowDetailController(tempBorrow.get(0));
                detail.showView();
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Nejprve vyberte knihy", "Není označena žádná kniha", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Nastaví datum od pro všechny spojené vypůjčené knihy
     */
    private void setDateTo() {
        DatePicker dp2 = new DatePicker(null, true);
        dp2.setLocationRelativeTo(null);
        dp2.setVisible(true);

        if (dp2.getDate() != null) {
            Date d2 = dp2.getDate();
            dialog.getINPto().setText(DateHelper.dateToString(d2, false));
        }
    }

    /**
     * Nastaví datum do pro všechny spojené vypůjčené knihy
     */
    private void setDateFrom() {
        DatePicker dp = new DatePicker(null, true);
        dp.setLocationRelativeTo(null);
        dp.setVisible(true);

        if (dp.getDate() != null) {
            Date d = dp.getDate();
            dialog.getINPfrom().setText(DateHelper.dateToString(d, false));
        }
    }

    /**
     * vygeneruje čárový kód půjčky
     */
    private void generateBarcode() {
        BufferedImage barcode = Barcode.encode(borrow.getBorrowCode());
        FileManager.getInstance().saveImage("ba_borrow_" + borrow.getBorrowCode(), barcode);
        FileManager.getInstance().openImage("ba_borrow_" + borrow.getBorrowCode());
    }

    /**
     * vygeneruje qr kód půjčky
     */
    private void generateQRCode() {
        BufferedImage qrcode = QRCode.encode(borrow.getBorrowCode());
        FileManager.getInstance().saveImage("qr_borrow_" + borrow.getBorrowCode(), qrcode);
        FileManager.getInstance().openImage("qr_borrow_" + borrow.getBorrowCode());
    }

    /**
     * Vytiskne informace o půjčce do PDF
     */
    private void printPDF() {
        PDFPrinter.getInstance().printBorrow(borrow);
    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     */
    private class BorrowDetailMouseListener implements MouseListener {

        public BorrowDetailMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Book b = (Book) tableModel.getBook(dialog.getTABbooks().getSelectedRow());
                BookDetailController bdc = new BookDetailController(b);
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

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
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
                    dispose();
                    break;
                case "printPDF":
                    printPDF();
                    break;
                case "qrcode":
                    generateQRCode();
                    break;
                case "barcode":
                    generateBarcode();
                    break;
                case "dateFrom":
                    setDateFrom();
                    break;
                case "dateTo":
                    setDateTo();
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
    }
}
