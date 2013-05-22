package controllers;

import coding.Barcode;
import coding.QRCode;
import helpers.DateHelper;
import helpers.Validator;
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
 * Třída (controller) starající se o pohled na detail knihy
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class BookDetailController extends BaseController {

    private BookDetailDialog dialog; // připojený pohled
    private BorrowTableModel tableModel; // tabulka výpůjček této knihy
    private boolean editMode; // indikace upravování knihy
    DefaultListModel<Author> authorListModel = new DefaultListModel(); // seznam autorů pro pohled
    Book book; // aktuálně prohlížená kniha

    /**
     * Konstruktor třídy nastaví aktuálně prohlíženou knihu
     *
     * @param book kniha k prohlížení
     */
    public BookDetailController(Book book) {
        dialog = new BookDetailDialog(null, true);
        this.book = BookService.getInstance().getBookWithCode(book.getBarcode());
        editMode = false;
        tableModel = new BorrowTableModel(BorrowService.getInstance().getBorrowsOfBook(book));
        initListeners();
        updateData();
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
        // ActionListener
        BookDetailActionListener a = new BookDetailActionListener();
        dialog.getBTNedit().addActionListener(a);
        dialog.getBTNclose().addActionListener(a);
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
        BookDetailMouseListener m = new BookDetailMouseListener();
        dialog.getTABlastBorrow().addMouseListener(m);

        //KeyListener
        BookDetailKeyListener k = new BookDetailKeyListener();
        dialog.getINPgenre().addKeyListener(k);
    }

    /**
     * Update dat v pohledu
     */
    private void updateData() {

        // Získá stejné položky        
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        List<Book> books = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), true);

        for (Book b : books) {
            comboBoxModel.addElement(b.getBarcode());
        }
        comboBoxModel.setSelectedItem(book.getBarcode());
        dialog.getINPselectTargetBook().setModel(comboBoxModel);

        // Titul
        dialog.getINPtitle().setText(book.getTitle());

        // Autori
        authorListModel = new DefaultListModel();
        for (Author a : book.getAuthors()) {
            authorListModel.add(authorListModel.getSize(), a);
        }
        dialog.getINPauthors().setModel(authorListModel);

        // Jazyk
        dialog.getINPlanguage().setText(book.getLanguage());

        // Vydavatel
        dialog.getINPpublisher().setText(book.getPublisher());

        // Vydáno roku
        if (book.getPublishedYear() != null) {
            dialog.getINPpublishedYear().setText(DateHelper.dateToString(book.getPublishedYear(), true));
        } else {
            dialog.getINPpublishedYear().setText("");
        }
        // ISBN 10
        dialog.getINPisbn10().setText(book.getISBN10());

        //ISBN 13
        dialog.getINPisbn13().setText(book.getISBN13());

        // Pocet stran
        dialog.getINPpageCount().setText(String.valueOf(book.getPageCount()));

        // Zanr
        String tempGenre = "";
        for (Genre g : book.getGenres()) {
            if (tempGenre.isEmpty()) {
                tempGenre = g.getGenre();
            } else {
                tempGenre += ";" + g.getGenre();
            }
        }

        dialog.getINPgenre().setText(tempGenre);

        // Sponzor
        dialog.getINPsponsor().setText(book.getSponsor());

        // Zakoupeno dne
        dialog.getINPaddedDate().setText(DateHelper.dateToString(book.getAddedDate(), false));

        // Poznamky
        dialog.getINPnotes().setText(book.getNotes());

        // Sklad
        int borrowed = BookService.getInstance().getBorrowed(book.getVolumeCode());
        int count = BookService.getInstance().getCount(book.getVolumeCode());

        dialog.getINPstock().setText(String.valueOf(count - borrowed) + "/" + String.valueOf(count));
        dialog.getINPlocation().setText(book.getLocation());

        // Nahled
        GoogleImageDownload gid = new GoogleImageDownload(dialog.getINPthumbnail(), 173, 263);
        if (book.getISBN10() == null && book.getISBN13() == null) {
            dialog.getINPthumbnail().setText("Náhled není k dispozici :-(");
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
        dialog.getTABlastBorrow().setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    /**
     * Smazání knihy
     */
    private void deleteAction() {
        int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete smazat knihu " + book.getBarcode() + "?", "Opravdu smazat?", JOptionPane.OK_CANCEL_OPTION);
        if (isSure == JOptionPane.OK_OPTION) {
            BookService.getInstance().delete(book.getBarcode());
            if (BookService.getInstance().getCount(book.getVolumeCode()) == 0) {
                dispose();
            } else {
                book = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), false).get(0);
                updateData();
            }
        }
    }

    /**
     * Přepnutí pohledu z prohlížení do editace a zpět
     */
    private void switchEditMode() {
        if (editMode) {
            dialog.getBTNedit().setName("editButton");
            dialog.getBTNedit().setText("Upravit");
            dialog.getINPnotes().setBackground(new Color(240, 240, 240));
            dialog.getINPauthors().setBackground(new Color(240, 240, 240));
            dialog.getINPaddedDate().setBackground(new Color(240, 240, 240));

        } else {
            dialog.getBTNedit().setName("saveButton");
            dialog.getBTNedit().setText("Uložit");
            dialog.getINPnotes().setBackground(Color.white);
            dialog.getINPauthors().setBackground(Color.white);
            dialog.getINPaddedDate().setBackground(Color.white);
        }

        dialog.getBTNaddAuthor().setVisible(!editMode);
        dialog.getBTNremoveAuthors().setVisible(!editMode);
        dialog.getBTNsetPublishedYear().setVisible(!editMode);
        dialog.getINPtitle().setEditable(!editMode);
        dialog.getINPgenre().setEditable(!editMode);
        dialog.getINPisbn10().setEditable(!editMode);
        dialog.getINPisbn13().setEditable(!editMode);
        dialog.getINPlanguage().setEditable(!editMode);
        dialog.getINPlocation().setEditable(!editMode);
        dialog.getINPnotes().setEditable(!editMode);
        dialog.getINPpageCount().setEditable(!editMode);
        dialog.getINPpublisher().setEditable(!editMode);
        dialog.getINPpublishedYear().setEditable(!editMode);
        dialog.getINPsponsor().setEditable(!editMode);
        dialog.getINPselectTargetBook().setEnabled(editMode);

        editMode = !editMode;

        dialog.revalidate();
        dialog.repaint();
    }

    /**
     * Obnovení smazané knihy
     */
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
        updateData();
    }

    /**
     * Uložení změn v knize
     */
    private void saveBook() {
        // Získání dat z pohledu
        String title = dialog.getINPtitle().getText().trim();
        String publisher = dialog.getINPpublisher().getText().trim();
        String language = dialog.getINPlanguage().getText().trim();
        String isbn10 = dialog.getINPisbn10().getText().trim();
        String isbn13 = dialog.getINPisbn13().getText().trim();
        String sponsor = dialog.getINPsponsor().getText().trim();
        String location = dialog.getINPlocation().getText().trim();
        String pageCount = dialog.getINPpageCount().getText().trim();
        String genre = dialog.getINPgenre().getText().trim();
        String notes = dialog.getINPnotes().getText().trim();
        Date publishedYear = DateHelper.stringToDate(dialog.getINPpublishedYear().getText(), true);
        Date addedDate = DateHelper.stringToDate(dialog.getINPaddedDate().getText(), false);

        /* VALIDACE */
        StringBuilder errorOutput = new StringBuilder();
        Configuration cfg = Configuration.getInstance();

        // Validace titulu
        if (Validator.isValidString(title)) {
            book.setTitle(title);
        } else {
            errorOutput.append("- Vyplňte název knihy\n");
        }

        // Validace autorů
        if (authorListModel.isEmpty()) {
            if (cfg.isRequireAuthor()) {
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

        // Validace vydavatele 
        if (Validator.isValidString(publisher)) {
            book.setPublisher(publisher);
        } else {
            if (cfg.isRequirePublisher()) {
                errorOutput.append("- Vyplňte vydavatele \n");
            }
        }

        // Validace jazyka
        if (Validator.isValidString(language)) {
            book.setLanguage(language);
        } else {
            if (cfg.isRequireLanguage()) {
                errorOutput.append("- Vyplňte jazyk \n");
            }
        }

        // Validace roku vydání
        if (publishedYear != null) {
            book.setPublishedYear(publishedYear);
        } else {
            if (cfg.isRequirePublishedYear()) {
                errorOutput.append("- Vyplňte rok vydání \n");
            }
        }

        // Validace ISBN10
        if (Validator.isValidISBN10(isbn10)) {
            book.setISBN10(isbn10);
        } else {
            if (cfg.isRequireISBN10()) {
                errorOutput.append("- Vyplňte správné ISBN10\n");
            }
        }

        // Validace ISBN13
        if (Validator.isValidISBN13(isbn13)) {
            book.setISBN13(isbn13);
        } else {
            if (cfg.isRequireISBN13()) {
                errorOutput.append("- Vyplňte správné ISBN13\n");
            }
        }

        // Validace sponzora
        if (Validator.isValidString(sponsor)) {
            book.setSponsor(sponsor);
        } else {
            if (cfg.isRequireSponsor()) {
                errorOutput.append("- vyplňte sponzora\n");
            }
        }

        // Validace data přidání
        if (addedDate == null) {
            if (cfg.isRequireAddedDate()) {
                errorOutput.append("- Zkontrolujte zadané datum přidání\n");
            }
        } else {
            book.setAddedDate(addedDate);
        }

        // Validace umístění 
        if (Validator.isValidString(location)) {
            book.setLocation(location);
        } else {
            if (cfg.isRequireLocation()) {
                errorOutput.append("Vyplňte prosím umístění\n");
            }
        }

        // Validace počtu stránek
        if (Validator.isValidPositiveNumber(pageCount)) {
            book.setPageCount(Integer.valueOf(pageCount));
        } else {
            if (cfg.isRequirePageCount()) {
                errorOutput.append("Zkonrolujte zadaný počet stránek");
            }
        }

        // Validace žánrů
        if (Validator.isValidString(genre)) {
            List<Genre> genres = new ArrayList<>();

            String[] tempGenres = dialog.getINPgenre().getText().split(";");

            for (String s : tempGenres) {
                Genre g = GenreService.getInstance().findGenre(s);
                if (g == null) {
                    errorOutput.append("žánr ").append(s).append(" neexistuje - přidat jej můžete v nastavení");
                    break;
                }
                genres.add(g);
            }
            if (genres.isEmpty() && cfg.isRequireGenre()) {
                errorOutput.append("Vyplňte žánr");
            } else {
                book.setGenres(genres);
            }
        } else {
            if (cfg.isRequireGenre()) {
                errorOutput.append("Vyplňte žánr");
            }
        }

        // Položky nepodléhající kontrole
        book.setNotes(notes);

        // Kontrola validace
        if (errorOutput.length() == 0) {
            BookService.getInstance().save(book);
            RefreshController.getInstance().refreshBookTab();
            ApplicationLog.getInstance().addMessage("Změny uloženy - " + book.getTitle() + " (" + book.getBarcode() + ")");
            switchEditMode();
        } else {
            JOptionPane.showMessageDialog(dialog, errorOutput.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Odstraní autory ze seznamu
     */
    private void removeAuthors() {
        int[] selectedIdx = dialog.getINPauthors().getSelectedIndices();
        for (int i = selectedIdx.length - 1; i >= 0; i--) {
            authorListModel.remove(selectedIdx[i]);
        }
        dialog.getINPauthors().setModel(authorListModel);
    }

    /**
     * Provede inventuru knihy
     */
    private void checkItem() {
        int isSure = JOptionPane.showInternalConfirmDialog(dialog.getContentPane(), "Opravdu chcete inventarozovat knihu " + book.getBarcode() + "?", "Potvrzení inventarizace", JOptionPane.OK_CANCEL_OPTION);
        if (isSure == JOptionPane.OK_OPTION) {
            book.setInventoriedDate(DateHelper.stringToDate(DateHelper.dateToString(new Date(), false), false));
            if (book.isDeleted()) {
                book.setDeleted(false);
            }
            BookService.getInstance().save(book);
            book = BookService.getInstance().getBooksByVolumeCode(book.getVolumeCode(), false).get(0);
            updateData();
            ApplicationLog.getInstance().addMessage("Kniha byla inventarizována - " + book.getTitle() + " (" + book.getBarcode() + ")");
        }
    }

    /**
     * Přidá autora do seznamu
     */
    private void addAuthor() {
        AddAuthorController aac = new AddAuthorController();
        aac.showView();
        if (aac.getAuthor() != null) {
            authorListModel.add(authorListModel.getSize(), aac.getAuthor());
            dialog.getINPauthors().setModel(authorListModel);
        }
    }

    /**
     * Zobrazí DatePicker pro datum publikace
     */
    private void setPublishedYear() {
        DatePicker dp = new DatePicker(null, true);
        dp.setLocationRelativeTo(null);
        dp.setVisible(true);

        if (dp.getDate() != null) {
            Date d = dp.getDate();
            dialog.getINPaddedDate().setText(DateHelper.dateToString(d, false));
        }
    }

    /**
     * Změní označenou položku
     */
    private void selectItem() {
        book = BookService.getInstance().getBookWithCode(dialog.getINPselectTargetBook().getModel().getSelectedItem().toString());
        updateData();
    }

    /**
     * Vygeneruje čárový kód
     */
    private void generateBarcode() {
        BufferedImage barcode = Barcode.encode(book.getBarcode());
        FileManager.getInstance().saveImage("ba_book_" + book.getBarcode(), barcode);
        FileManager.getInstance().openImage("ba_book_" + book.getBarcode());
    }

    /**
     * Vygeneruje QR kód
     */
    private void generateQRCode() {
        BufferedImage qrcode = QRCode.encode(book.getBarcode());
        FileManager.getInstance().saveImage("qr_book_" + book.getBarcode(), qrcode);
        FileManager.getInstance().openImage("qr_book_" + book.getBarcode());
    }

    /**
     * Vytiskne záznam knihy do PDF
     */
    private void printPDF() {
        PDFPrinter.getInstance().printBook(book);
    }

    /**
     * Třída zodpovídající za stisk klávesy z odposlouchávaných komponent
     * pohledu
     */
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
                    if (dialog.getINPgenre().getText().endsWith(" ")) {
                        dialog.getINPgenre().setText(dialog.getINPgenre().getText().trim());
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
                    String in = dialog.getINPgenre().getText().trim();
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
                                dialog.getINPgenre().setText(before + g.getGenre());
                                // oznaci doplnene
                                dialog.getINPgenre().setSelectionStart(before.length() + start);
                                dialog.getINPgenre().setSelectionEnd(dialog.getINPgenre().getText().length());
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     */
    private class BookDetailMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Borrow b = (Borrow) tableModel.getBorrow(dialog.getTABlastBorrow().getSelectedRow());
                BorrowDetailController bdc = new BorrowDetailController(b);
                bdc.showView();
                updateData();
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
                    printPDF();
                    break;
                case "qrcode":
                    generateQRCode();
                    break;
                case "barcode":
                    generateBarcode();
                    break;
                case "selectItem":
                    selectItem();
                    break;
                case "publishedYear":
                    setPublishedYear();
                    break;
                case "addAuthor":
                    addAuthor();
                    break;
                case "checkItem":
                    checkItem();
                    break;
                case "removeAuthors":
                    removeAuthors();
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }
}
