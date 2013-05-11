/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import coding.Barcode;
import coding.QRCode;
import helpers.DateHelper;
import io.ApplicationLog;
import io.Configuration;
import io.FileManager;
import io.PDFPrinter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.BorrowTableModel;
import models.entity.Author;
import models.entity.Book;
import models.entity.Borrow;
import models.entity.Genre;
import remote.GoogleImageDownload;
import services.AuthorService;
import services.BookService;
import services.BorrowService;
import services.GenreService;
import views.BookDetailDialog;
import views.DatePicker;

/**
 *
 * @author Nesh
 */
public class BookDetailController extends BaseController {

    private BookDetailDialog dialog;
    private BorrowTableModel tableModel;
    private boolean editMode;
    DefaultListModel<Author> authorListModel = new DefaultListModel();
    Book book;

    public BookDetailController(Book book) {
        dialog = new BookDetailDialog(null, true);
        this.book = BookService.getInstance().getBookWithCode(book.getBarcode());
        editMode = false;
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfBook(book));
        initListeners();
        showData();
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
        // ActionListener
        BookDetailActionListener a = new BookDetailActionListener();
        dialog.getEditButton().addActionListener(a);
        dialog.getCloseButton().addActionListener(a);
        dialog.getBTNdelete().addActionListener(a);
        dialog.getINPselectTargetBook().addActionListener(a);
        dialog.getBTNaddAuthor().addActionListener(a);
        dialog.getBTNremoveAuthors().addActionListener(a);
        dialog.getBTNsetPublishedYear().addActionListener(a);
        dialog.getBTNcheckItem().addActionListener(a);
        dialog.getBTNbarcode().addActionListener(a);
        dialog.getBTNqrcode().addActionListener(a);
        dialog.getBTNprint().addActionListener(a);
        dialog.getBTNrenew().addActionListener(a);

        //MouseListener
        BookDetailListListener m = new BookDetailListListener();
        dialog.getLastBorrowTable().addMouseListener(m);

        BookDetailKeyListener k = new BookDetailKeyListener();
        dialog.getInfoGenre().addKeyListener(k);
    }

    private void showData() {

        // Stejné položky        
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        List<Book> books = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), true);

        for (Book b : books) {
            comboBoxModel.addElement(b.getBarcode());
        }
        comboBoxModel.setSelectedItem(book.getBarcode());
        dialog.getINPselectTargetBook().setModel(comboBoxModel);

        // Titul
        dialog.getInfoTitle().setText(book.getTitle());

        // Autori
        authorListModel = new DefaultListModel();
        for (Author a : book.getAuthors()) {
            authorListModel.add(authorListModel.getSize(), a);
        }
        dialog.getInfoAuthors().setModel(authorListModel);

        // Jazyk
        dialog.getInfoLanguage().setText(book.getLanguage());

        // Vydavatel
        dialog.getInfoPublisher().setText(book.getPublisher());

        // Vydáno roku
        if (book.getPublishedYear() != null) {
            dialog.getInfoPublishedYear().setText(DateHelper.dateToString(book.getPublishedYear(), true));
        } else {
            dialog.getInfoPublishedYear().setText("");
        }
        // ISBN 10
        dialog.getInfoISBN10().setText(book.getISBN10());

        //ISBN 13
        dialog.getInfoISBN13().setText(book.getISBN13());

        // Pocet stran
        dialog.getInfoPageCount().setText(String.valueOf(book.getPageCount()));

        // Zanr
        String tempGenre = "";
        for (Genre g : book.getGenres()) {
            if (tempGenre.isEmpty()) {
                tempGenre = g.getGenre();
            } else {
                tempGenre += ";" + g.getGenre();
            }
        }

        dialog.getInfoGenre().setText(tempGenre);

        // Sponzor
        dialog.getInfoSponsor().setText(book.getSponsor());

        // Zakoupeno dne
        dialog.getInfoBuyedDate().setText(DateHelper.dateToString(book.getAddedDate(), false));

        // Poznamky
        dialog.getInfoNotes().setText(book.getNotes());

        // Sklad
        int borrowed = BookService.getInstance().getBorrowed(book.getVolumeCode());
        int count = BookService.getInstance().getCount(book.getVolumeCode());

        dialog.getInfoStock().setText(String.valueOf(count - borrowed) + "/" + String.valueOf(count));
        dialog.getInfoLocation().setText(book.getLocation());

        // Nahled 

        GoogleImageDownload gid = new GoogleImageDownload(dialog.getInfoThumb(), 173, 263);
        if (book.getISBN10() == null && book.getISBN13() == null) {
            dialog.getInfoThumb().setText("Náhled není k dispozici :-(");
        } else {
            if (book.getISBN13() == null) {
                gid.setISBN(book.getISBN10());
                gid.start();
            } else {
                gid.setISBN(book.getISBN13());
                gid.start();
            }
        }


        dialog.getBTNrenew().setVisible(book.isDeleted());
        dialog.getBTNdelete().setVisible(!book.isDeleted());
        dialog.getBTNcheckItem().setVisible(!book.isDeleted());


        // Posledni výpůjčky
        dialog.getLastBorrowTable().setModel(tableModel);
        tableModel.fireTableDataChanged();

    }

    private class BookDetailKeyListener implements KeyListener {

        List<Genre> genres;

        public BookDetailKeyListener() {
            genres = GenreService.getInstance().getList();
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

                case "genres":
                    //potlaceni mezer
                    if (dialog.getInfoGenre().getText().endsWith(" ")) {
                        dialog.getInfoGenre().setText(dialog.getInfoGenre().getText().trim());
                        return;
                    }

                    // pokud se nezapise znak - hned skonci
                    if (String.valueOf(e.getKeyChar()).trim().isEmpty()) {
                        return;
                    }

                    if (e.getKeyChar() == ';') {
                        return;
                    }

                    // priprava promennych 
                    String in = dialog.getInfoGenre().getText().trim();
                    String before = "";

                    if (in.contains(";")) {
                        int lastNewWord = in.lastIndexOf(';');
                        before = in.substring(0, lastNewWord + 1);
                        in = in.substring(lastNewWord + 1);
                    }

                    int start = in.length();

                    // zadany aspon 2 znaky
                    if (start > 1) {
                        for (Genre g : genres) {
                            if (g.getGenre() != null && g.getGenre().toLowerCase().startsWith(in.toLowerCase())) {
                                dialog.getInfoGenre().setText(before + g.getGenre());
                                // oznaci doplnene
                                dialog.getInfoGenre().setSelectionStart(before.length() + start);
                                dialog.getInfoGenre().setSelectionEnd(dialog.getInfoGenre().getText().length());
                                break;
                            }
                        }
                    }
                    break;
            }

        }
    }

    private class BookDetailListListener implements MouseListener {

        public BookDetailListListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(dialog.getLastBorrowTable().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
                showData();
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

    private class BookDetailActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "editButton":
                    switchEditMode();
                    break;
                case "saveButton":
                    saveBook();
                    break;
                case "renew":
                    renew();
                    break;
                case "delete":
                    deleteAction();
                    break;
                case "closeButton":
                    dispose();
                    break;
                case "printPDF":
                    PDFPrinter.getInstance().printBook(book);
                    break;
                case "qrcode":
                    BufferedImage qrcode = QRCode.encode(book.getBarcode());
                    FileManager.getInstance().saveImage("qr_book_" + book.getBarcode(), qrcode);
                    FileManager.getInstance().openImage("qr_book_" + book.getBarcode());
                    break;
                case "barcode":
                    BufferedImage barcode = Barcode.encode(book.getBarcode());
                    FileManager.getInstance().saveImage("ba_book_" + book.getBarcode(), barcode);
                    FileManager.getInstance().openImage("ba_book_" + book.getBarcode());
                    break;
                case "selectItem":
                    book = BookService.getInstance().getBookWithCode(dialog.getINPselectTargetBook().getModel().getSelectedItem().toString());
                    showData();
                    break;
                case "publishedYear":
                    DatePicker dp = new DatePicker(null, true);
                    dp.setLocationRelativeTo(null);
                    dp.setVisible(true);

                    if (dp.getDate() != null) {
                        Date d = dp.getDate();
                        dialog.getInfoBuyedDate().setText(DateHelper.dateToString(d, false));
                    }
                    break;
                case "addAuthor":
                    AddAuthorController aac = new AddAuthorController();
                    aac.showView();
                    if (aac.getAuthor() != null) {
                        authorListModel.add(authorListModel.getSize(), aac.getAuthor());
                        dialog.getInfoAuthors().setModel(authorListModel);
                    }
                    break;

                case "checkItem":
                    int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete inventarozovat knihu " + book.getBarcode() + "?", "Potvrzení inventarizace", JOptionPane.OK_CANCEL_OPTION);
                    if (isSure == JOptionPane.OK_OPTION) {
                        book.setInventoriedDate(DateHelper.stringToDate(DateHelper.dateToString(new Date(), false), false));
                        if (book.isDeleted()) {
                            book.setDeleted(false);
                        }
                        BookService.getInstance().save(book);
                        book = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), false).get(0);
                        showData();
                        ApplicationLog.getInstance().addMessage("Kniha byla inventarizována - " + book.getTitle() + " (" + book.getBarcode() + ")");

                    }
                    break;
                case "removeAuthors":
                    int[] selectedIdx = dialog.getInfoAuthors().getSelectedIndices();
                    for (int i = selectedIdx.length - 1; i >= 0; i--) {
                        authorListModel.remove(selectedIdx[i]);
                    }
                    dialog.getInfoAuthors().setModel(authorListModel);
                    break;
                default:
                    break;
            }
        }

        private void deleteAction() {

            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete smazat knihu " + book.getBarcode() + "?", "Opravdu smazat?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                BookService.getInstance().delete(book.getBarcode());
                if (BookService.getInstance().getCount(book.getVolumeCode()) == 0) {
                    dispose();
                } else {
                    book = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), false).get(0);
                    showData();
                }
            } else {
                return;
            }
        }

        private void switchEditMode() {
            if (editMode) {
                dialog.getEditButton().setName("editButton");
                dialog.getEditButton().setText("Upravit");
                dialog.getInfoNotes().setBackground(new Color(240, 240, 240));
                dialog.getInfoAuthors().setBackground(new Color(240, 240, 240));
                dialog.getInfoBuyedDate().setBackground(new Color(240, 240, 240));

            } else {
                dialog.getEditButton().setName("saveButton");
                dialog.getEditButton().setText("Uložit");
                dialog.getInfoNotes().setBackground(Color.white);
                dialog.getInfoAuthors().setBackground(Color.white);
                dialog.getInfoBuyedDate().setBackground(Color.white);
            }

            dialog.getBTNaddAuthor().setVisible(!editMode);
            dialog.getBTNremoveAuthors().setVisible(!editMode);
            dialog.getBTNsetPublishedYear().setVisible(!editMode);
            dialog.getInfoTitle().setEditable(!editMode);
            dialog.getInfoGenre().setEditable(!editMode);
            dialog.getInfoISBN10().setEditable(!editMode);
            dialog.getInfoISBN13().setEditable(!editMode);
            dialog.getInfoLanguage().setEditable(!editMode);
            dialog.getInfoLocation().setEditable(!editMode);
            dialog.getInfoNotes().setEditable(!editMode);
            dialog.getInfoPageCount().setEditable(!editMode);
            dialog.getInfoPublisher().setEditable(!editMode);
            dialog.getInfoPublishedYear().setEditable(!editMode);
            dialog.getInfoSponsor().setEditable(!editMode);
            dialog.getINPselectTargetBook().setEnabled(editMode);


            editMode = !editMode;

            dialog.revalidate();
            dialog.repaint();
        }

        private void renew() {
            int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete obnovit knihu " + book.getTitle() + " (" + book.getBarcode() + ")?", "Opravdu obnovit?", JOptionPane.OK_CANCEL_OPTION);
            if (isSure == JOptionPane.OK_OPTION) {
                book.setDeleted(false);
                BookService.getInstance().save(book);
                ApplicationLog.getInstance().addMessage("Kniha " + book.getTitle() + " (" + book.getBarcode() + ") byla obnovena");
                RefreshController.getInstance().refreshBookTab();
                dialog.getBTNrenew().setVisible(false);
                dialog.getBTNdelete().setVisible(true);
                dialog.getBTNcheckItem().setVisible(true);
            }
            showData();
        }

        private void saveBook() {
            StringBuilder errorOutput = new StringBuilder();

            // TITUL
            if (dialog.getInfoTitle().getText() == null || dialog.getInfoTitle().getText().trim().isEmpty()) {
                errorOutput.append("- Vyplňte název knihy\n");
            } else {
                book.setTitle(dialog.getInfoTitle().getText());
            }

            // AUTORI
            if (authorListModel.isEmpty()) {
                if (Configuration.getInstance().isRequireAuthor()) {
                    errorOutput.append("- Vyplňte autora \n");
                }
            } else {
                ArrayList<Author> temp = new ArrayList<>();
                for (int i = 0; i < authorListModel.getSize(); i++) {
                    temp.add(AuthorService.getInstance().getOrCreate(authorListModel.get(i)));
                }
                book.setAuthors(temp);
                book.setMainAuthor(temp.get(0));
            }

            //vydavatel 
            if (dialog.getInfoPublisher().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequirePublisher()) {
                    errorOutput.append("- Vyplňte vydavatele \n");
                }
            } else {
                book.setPublisher(dialog.getInfoPublisher().getText());
            }

            //jazyk
            if (dialog.getInfoLanguage().getText().trim().isEmpty()) {

                if (Configuration.getInstance().isRequireLanguage()) {
                    errorOutput.append("- Vyplňte jazyk \n");
                }
            } else {
                book.setLanguage(dialog.getInfoLanguage().getText());
            }

            //rok vydání

            Date d = DateHelper.stringToDate(dialog.getInfoPublishedYear().getText(), true);

            if (d == null) {
                if (Configuration.getInstance().isRequirePublishedYear()) {
                    errorOutput.append("- Vyplňte rok vydání \n");
                }
            } else {
                book.setPublishedYear(d);
            }
            // ISBN 10
            if (dialog.getInfoISBN10().getText() == null || dialog.getInfoISBN10().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireISBN10()) {
                    errorOutput.append("- Vyplňte ISBN 10\n");
                }
            } else {
                book.setISBN10(dialog.getInfoISBN10().getText());
            }

            // ISBN 13
            if (dialog.getInfoISBN13().getText() == null || dialog.getInfoISBN13().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireISBN13()) {
                    errorOutput.append("- vyplňte ISBN 13\n");
                }
            } else {
                book.setISBN13(dialog.getInfoISBN13().getText());
            }

            // datum pridani
            Date d2 = DateHelper.stringToDate(dialog.getInfoBuyedDate().getText(), false);
            if (d2 == null) {
                if (Configuration.getInstance().isRequireAddedDate()) {
                    errorOutput.append("- Zkontrolujte zadané datum přidání\n");
                }
            } else {
                book.setAddedDate(d2);
            }

            // Umístění 
            if (dialog.getInfoLocation().getText() == null || dialog.getInfoLocation().getText().trim().isEmpty()) {
                if (Configuration.getInstance().isRequireLocation()) {
                    errorOutput.append("Vyplňte prosím umístění\n");
                }
            } else {
                book.setLocation(dialog.getInfoLocation().getText());
            }

            //pages 
            try {
                book.setPageCount(Integer.valueOf(dialog.getInfoPageCount().getText()));
            } catch (NumberFormatException e) {
                if (Configuration.getInstance().isRequirePageCount()) {
                    errorOutput.append("Zkonrolujte zadaný počet stránek");
                }
            }
            //zanry
            if (!dialog.getInfoGenre().getText().trim().isEmpty()) {
                List<Genre> genres = new ArrayList<>();

                String[] tempGenres = dialog.getInfoGenre().getText().split(";");

                for (String s : tempGenres) {
                    Genre g = GenreService.getInstance().findGenre(s);
                    if (g == null) {
                        errorOutput.append("žánr ").append(s).append(" neexistuje - přidat jej můžete v nastavení");
                        break;
                    }
                    genres.add(g);
                }
                if (genres.isEmpty() && Configuration.getInstance().isRequireGenre()) {
                    errorOutput.append("Vyplňte žánr");
                } else {
                    book.setGenres(genres);
                }
            } else {
                if (Configuration.getInstance().isRequireGenre()) {
                    errorOutput.append("Vyplňte žánr");
                }
            }

            // Check validation results
            if (errorOutput.length() == 0) {
                BookService.getInstance().save(book);
                RefreshController.getInstance().refreshBookTab();
                ApplicationLog.getInstance().addMessage("Změny uloženy - " + book.getTitle() + " (" + book.getBarcode() + ")");
                switchEditMode();
            } else {
                JOptionPane.showMessageDialog(dialog, errorOutput.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}
