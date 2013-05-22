package controllers;

import helpers.DateHelper;
import helpers.Validator;
import io.Configuration;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import models.BookTableModel;
import models.entity.Author;
import models.entity.Book;
import models.entity.Genre;
import remote.GoogleSearch;
import services.AuthorService;
import services.BookService;
import services.GenreService;
import views.DatePicker;
import views.NewBookDialog;
import views.SearchResultsDialog;

/**
 * Třída (controller) starající se o vytvoření nové knihy
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class NewBookController extends BaseController {

    private NewBookDialog dialog; //  připojený pohled
    private SearchResultsDialog resOut; // dialog výsledků
    private GoogleSearch gbs; // Třída pro hledání knih
    private Book selectedBoook; // nová kniha
    private ArrayList<Book> foundedItems; // seznam nalezených knih
    DefaultListModel<Author> authorListModel = new DefaultListModel(); // seznam autorů

    /**
     * Třídní konstruktor
     *
     * @param parent hlavní pohled
     */
    public NewBookController(JFrame parent) {
        gbs = new GoogleSearch(this);
        dialog = new NewBookDialog(parent, false);
        
        // Pripravi dialog pro zobrazení výsledku
        resOut = new SearchResultsDialog(null, true);
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
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
        NewBookActionListener a = new NewBookActionListener();
        dialog.getBTNsave().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNaddAuthor().addActionListener(a);
        dialog.getBTNremoveAuthor().addActionListener(a);
        dialog.getBTNbuyedDate().addActionListener(a);
        dialog.getBTNsearch().addActionListener(a);


        NewBookKeyListener k = new NewBookKeyListener();
        dialog.getINPauthors().addKeyListener(k);
        dialog.getINPgenres().addKeyListener(k);

        resOut.getResultsTable().addMouseListener(new NewBookMouseListener());
    }

    /**
     * Zobrazení nalezených výsledků
     */
    public void showResults() {
        // Znovu zpristupni tlacitko pro hledaní
        dialog.getBTNsearch().setEnabled(true);
        dialog.getBTNsearch().setText("Hledat znovu");
        dialog.getBTNsearch().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));

        if (gbs.getResultsCount() > 0) {



            // Ulozi nalezene vysledky do pole 
            foundedItems = new ArrayList<>();
            foundedItems.addAll(gbs.getResults());

            // Pripravi table model a zobrazi vysledky
            BookTableModel ctm = new BookTableModel(foundedItems);
            resOut.getResultsTable().setModel(ctm);
            resOut.setLocationRelativeTo(null);
            resOut.setVisible(true);
        }


        gbs = new GoogleSearch(this);
    }

    /**
     * Selhání vyhledávání
     */
    public void searchFail() {
        dialog.getBTNsearch().setEnabled(true);
        dialog.getBTNsearch().setText("Chyba: Opakovat?");
        dialog.getBTNsearch().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));
    }

    /**
     * Uloží knihu
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
        String pageCount = String.valueOf(dialog.getINPpageCount().getValue());
        String genre = dialog.getINPgenres().getText().trim();
        Date publishedYear = (Date) dialog.getINPpublishedDate().getValue();
        Date addedDate = DateHelper.stringToDate(dialog.getINPbuyedDate().getText(), false);

        /* VALIDACE */
        StringBuilder errorOutput = new StringBuilder();
        Configuration cfg = Configuration.getInstance();
        Book book = new Book();
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
                errorOutput.append("- Vyplňte sponzora\n");
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
                errorOutput.append(" - Vyplňte umístění\n");
            }
        }

        // Validace počtu stránek
        if (Validator.isValidPositiveNumber(pageCount)) {
            book.setPageCount(Integer.valueOf(pageCount));
        } else {
            if (cfg.isRequirePageCount()) {
                errorOutput.append("- Zkonrolujte zadaný počet stránek");
            }
        }

        // Validace žánrů
        if (Validator.isValidString(genre)) {
            List<Genre> genres = new ArrayList<>();

            String[] tempGenres = dialog.getINPgenres().getText().split(";");

            for (String s : tempGenres) {
                Genre g = GenreService.getInstance().findGenre(s);
                if (g == null) {
                    errorOutput.append("žánr ").append(s).append(" neexistuje - přidat jej můžete v nastavení");
                    break;
                }
                genres.add(g);
            }
            if (genres.isEmpty() && cfg.isRequireGenre()) {
                errorOutput.append("- Vyplňte žánr");
            } else {
                book.setGenres(genres);
            }
        } else {
            if (cfg.isRequireGenre()) {
                errorOutput.append("- Vyplňte žánr");
            }
        }
        if (errorOutput.length() == 0) {
            ArrayList<Author> temp = new ArrayList<>();
            for (int i = 0; i < authorListModel.getSize(); i++) {
                temp.add(AuthorService.getInstance().getOrCreate(authorListModel.get(i)));
            }
            book.setAuthors(temp);
            book.setMainAuthor(temp.get(0));
            BookService.getInstance().saveBook(book, (int) dialog.getINPcount().getValue());
            // UPDATE TABULKY
            dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, errorOutput.toString(), "Zkontrolujte zadané údaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Přídání autora
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
     * Smazání autora
     */
    private void removeAuthor() {
        int[] selectedIdx;
        selectedIdx = dialog.getINPauthors().getSelectedIndices();
        for (int i = selectedIdx.length - 1; i >= 0; i--) {
            authorListModel.remove(selectedIdx[i]);
        }
        dialog.getINPauthors().setModel(authorListModel);
    }

    /**
     * Získání data
     */
    private void datePicker() {
        DatePicker dp = new DatePicker(null, true);
        dp.setLocationRelativeTo(null);
        dp.setVisible(true);

        if (dp.getDate() != null) {
            Date d = dp.getDate();
            dialog.getINPbuyedDate().setText(DateHelper.dateToString(d, false));
        }
    }

    /**
     * Vyhledávání
     */
    private void search() {
        // Zamezí opětovnému spuštění
        dialog.getBTNsearch().setEnabled(false);
        dialog.getBTNsearch().setText("Hledám...");
        dialog.getBTNsearch().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/waiting.gif")));

        // Nastavý hodnoty pro vyhledávání
        for (int i = 0; i < authorListModel.getSize(); i++) {
            gbs.setAutor(authorListModel.get(i).toString());
        }
        gbs.setTitle(dialog.getINPtitle().getText());
        gbs.setISBN(dialog.getINPisbn13().getText());
        gbs.setISBN(dialog.getINPisbn10().getText());

        if (gbs.getQuery().isEmpty()) {
            dialog.getBTNsearch().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/we-search.png")));
            dialog.getBTNsearch().setText("Hledat znovu");
            dialog.getBTNsearch().setEnabled(true);
            return;
        }

        // Vyhledávání
        gbs.start();
    }

    /**
     * Třída zodpovídající za stisk klávesy z odposlouchávaných komponent
     * pohledu
     */
    private class NewBookKeyListener implements KeyListener {

        List<Genre> genres;

        public NewBookKeyListener() {
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
                case "authors":
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        authorListModel.remove(dialog.getINPauthors().getSelectedIndex());
                        dialog.getINPauthors().setModel(authorListModel);
                    }
                    break;
                case "genres":
                    //potlaceni mezer
                    if (dialog.getINPgenres().getText().endsWith(" ")) {
                        dialog.getINPgenres().setText(dialog.getINPgenres().getText().trim());
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
                    String in = dialog.getINPgenres().getText().trim();

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
                                dialog.getINPgenres().setText(before + g.getGenre());
                                // oznaci doplnene
                                dialog.getINPgenres().setSelectionStart(before.length() + start);
                                dialog.getINPgenres().setSelectionEnd(dialog.getINPgenres().getText().length());
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
    private class NewBookActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "search":
                    search();
                    break;

                case "saveButton":
                    saveBook();
                    break;
                case "cancelButton":
                    dispose();
                    break;
                case "addAuthor":
                    addAuthor();
                    break;
                case "removeAuthor":
                    removeAuthor();
                    break;
                case "datePickerButton":
                    datePicker();
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     *
     */
    private class NewBookMouseListener implements MouseListener {

        public NewBookMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();
                JTable t = resOut.getResultsTable();
                int index = t.rowAtPoint(p);

                selectedBoook = foundedItems.get(index);

                if (selectedBoook.getTitle() != null) {
                    dialog.getINPtitle().setText(selectedBoook.getTitle());
                }
                if (!selectedBoook.getAuthors().isEmpty()) {
                    authorListModel.removeAllElements();
                    for (Author a : selectedBoook.getAuthors()) {
                        authorListModel.add(authorListModel.getSize(), a);
                    }
                    dialog.getINPauthors().setModel(authorListModel);
                }
                if (selectedBoook.getISBN10() != null) {
                    dialog.getINPisbn10().setText(selectedBoook.getISBN10());
                }
                if (selectedBoook.getISBN13() != null) {
                    dialog.getINPisbn13().setText(selectedBoook.getISBN13());
                }
                if (selectedBoook.getPageCount() != 0) {
                    dialog.getINPpageCount().setValue(selectedBoook.getPageCount());
                }
                if (selectedBoook.getLanguage() != null) {
                    dialog.getINPlanguage().setText(selectedBoook.getLanguage());
                }
                if (selectedBoook.getPublisher() != null) {
                    dialog.getINPpublisher().setText(selectedBoook.getPublisher());
                }
                if (selectedBoook.getPublishedYear() != null) {
                    dialog.getINPpublishedDate().setValue(selectedBoook.getPublishedYear());
                }
                resOut.dispose();
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
}
