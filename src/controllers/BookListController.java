package controllers;

import io.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import models.BookTableModel;
import models.entity.Book;
import services.BookService;
import views.BookFilterDialog;
import views.BookListDialog;

/**
 * Třída (controller) starající se o hledání v seznamu knih
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class BookListController extends BaseController {

    private BookListDialog dialog; // připojený pohled
    private ArrayList<Book> selectedBooks = new ArrayList<>(); // vybrané knihy
    private boolean selectionMode; // mód výběru
    private BookTableModel tableModel; // model tabulky nalezených knih
    private BookFilterDialog filter; // dialog zobrazení
    // ŘAZENÍ
    Map<String, String> orderType = BookService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = BookService.getInstance().getOrderByMap();

    /**
     * Konstruktor třídy
     */
    public BookListController(JFrame parent, boolean selectionMode) {
        dialog = new BookListDialog(parent, selectionMode);
        this.selectionMode = selectionMode;
        tableModel = new BookTableModel();
        dialog.getTABresults().setModel(tableModel);
        filter = new BookFilterDialog(null, true);
        setFilterData();
        initListeners();
        updateView();
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
        BookListActionListener a = new BookListActionListener();
        dialog.getBTNconfirm().addActionListener(a);
        dialog.getBTNcancel().addActionListener(a);
        dialog.getBTNfilter().addActionListener(a);
        dialog.getBTNsearch().addActionListener(a);
        dialog.getBTNprevPage().addActionListener(a);
        dialog.getBTNnextPage().addActionListener(a);
        dialog.getBTNreset().addActionListener(a);

        // KeyListener
        BookListKeyListener k = new BookListKeyListener();
        dialog.getINPpageNumber().addKeyListener(k);

        // MouseListener
        BookListMouseListener m = new BookListMouseListener();
        dialog.getTABresults().addMouseListener(m);
    }

    /**
     * Update dat v pohledu
     */
    private void updateView() {
        // UPDATE tabulky
        tableModel.setForBorrow(true);
        tableModel.updateData();
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update stránkování
        dialog.getINPpageNumber().setText(String.valueOf(tableModel.getPage()));
        dialog.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            dialog.getBTNprevPage().setEnabled(false);
        } else {
            dialog.getBTNprevPage().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            dialog.getBTNnextPage().setEnabled(false);
        } else {
            dialog.getBTNnextPage().setEnabled(true);
        }
    }

    /**
     * Metoda měnící zobrazení v tabulce
     */
    private void setFilterData() {
        filter.getTitleCheckbox().setSelected(Configuration.getInstance().isShowTitle());
        filter.getAuthorCheckbox().setSelected(Configuration.getInstance().isShowAuthor());
        filter.getISBN10Checkbox().setSelected(Configuration.getInstance().isShowISBN10());
        filter.getISBN13Checkbox().setSelected(Configuration.getInstance().isShowISBN13());
        filter.getCountCheckbox().setSelected(Configuration.getInstance().isShowCount());
        filter.getLocationCheckbox().setSelected(Configuration.getInstance().isShowLocation());
        filter.getLanguageCheckbox().setSelected(Configuration.getInstance().isShowLanguage());
        filter.getPublisherCheckbox().setSelected(Configuration.getInstance().isShowPublisher());
        filter.getPublishedDateCheckbox().setSelected(Configuration.getInstance().isShowPublishedYear());
        filter.getPageCountCheckbox().setSelected(Configuration.getInstance().isShowPageCount());

        filter.getINPbookMaxRowsCount().setValue((int) Configuration.getInstance().getMaxBookRowsCount());

        for (Map.Entry<String, String> entry : orderType.entrySet()) {
            filter.getINPorderType().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBookOrderType())) {
                filter.getINPorderType().setSelectedItem(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : orderBy.entrySet()) {
            filter.getINPorderBy().addItem(entry.getKey());
            if (entry.getValue().equals(Configuration.getInstance().getBookOrderBy())) {
                filter.getINPorderBy().setSelectedItem(entry.getKey());
            }
        }

        RefreshController.getInstance().refreshBookTab();
    }

    /**
     * Vrátí označené knihy
     *
     * @return označené knihy
     */
    public ArrayList<Book> getBooks() {
        return selectedBooks;
    }

    /**
     * Potvrdí dialog
     */
    private void confirmDialog() {
        if (dialog.getTABresults().getSelectedRows().length > 0 && selectionMode) {
            int[] selRows = dialog.getTABresults().getSelectedRows();
            for (int i = 0; i < selRows.length; i++) {
                selectedBooks.add(tableModel.getBook(selRows[i]));
            }
            dialog.dispose();
        }
    }

    /**
     * Smaže vyhledávací data
     */
    private void resetSearch() {
        dialog.getINPauthor().setText("");
        dialog.getINPbarcode().setText("");
        dialog.getINPisbn10().setText("");
        dialog.getINPisbn13().setText("");
        dialog.getINPpublishedYear().setText("");
        dialog.getINPtitle().setText("");
        updateView();
    }

    /**
     * Zobrazení dialogu pro nastavení zobrazení
     */
    private void showFilter() {
        filter.setLocationRelativeTo(null);
        filter.setVisible(true);
    }

    /**
     * Nastavení zobrazení knih
     */
    private void setSilter() {
        Configuration.getInstance().setShowTitle(filter.getTitleCheckbox().isSelected());
        Configuration.getInstance().setShowAuthor(filter.getAuthorCheckbox().isSelected());
        Configuration.getInstance().setShowPublisher(filter.getPublisherCheckbox().isSelected());
        Configuration.getInstance().setShowPublishedYear(filter.getPublishedDateCheckbox().isSelected());
        Configuration.getInstance().setShowLanguage(filter.getLanguageCheckbox().isSelected());
        Configuration.getInstance().setShowISBN10(filter.getISBN10Checkbox().isSelected());
        Configuration.getInstance().setShowISBN13(filter.getISBN13Checkbox().isSelected());
        Configuration.getInstance().setShowPageCount(filter.getPageCountCheckbox().isSelected());
        Configuration.getInstance().setShowCount(filter.getCountCheckbox().isSelected());
        Configuration.getInstance().setShowLocation(filter.getLocationCheckbox().isSelected());

        Configuration.getInstance().setBookOrderBy(orderBy.get((String) filter.getINPorderBy().getSelectedItem()));
        Configuration.getInstance().setBookOrderType(orderType.get((String) filter.getINPorderType().getSelectedItem()));
        Configuration.getInstance().setMaxBookRowsCount((int) filter.getINPbookMaxRowsCount().getValue());
        tableModel.updateData();
        tableModel.fireTableStructureChanged();
        filter.setVisible(false);
    }

    /**
     * Vyhledávání knih
     */
    private void search() {
        tableModel.search(
                dialog.getINPbarcode().getText().trim(),
                dialog.getINPtitle().getText().trim(),
                dialog.getINPauthor().getText().trim(),
                dialog.getINPisbn10().getText().trim(),
                dialog.getINPisbn13().getText().trim(),
                dialog.getINPpublishedYear().getText().trim());
        tableModel.stopSearchWithEmptyResult();
        updateView();
    }

    /**
     * Předchozí stránka v tabulce
     */
    private void prevPage() {
        tableModel.prevPage();
        updateView();
    }

    /**
     * Další stránka v tabulce
     */
    private void nextPage() {
        tableModel.nextPage();
        updateView();
    }

    /**
     * Nastaví stránku dle textového vstupu
     */
    private void setPageNumber() {

        String in = dialog.getINPpageNumber().getText();
        try {
            tableModel.setPage(Integer.parseInt(in));
        } catch (NumberFormatException ex) {
            System.out.println("NESPRAVNY FORMAT CISLA");
        }
        updateView();

    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     */
    private class BookListMouseListener implements MouseListener {

        public BookListMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                selectedBooks.add((Book) tableModel.getBook(dialog.getTABresults().getSelectedRow()));
            }
            if (selectionMode) {
                if (!selectedBooks.isEmpty()) {
                    dialog.dispose();
                }
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
     * Třída zodpovídající za stisk klávesy z odposlouchávaných komponent
     * pohledu
     */
    private class BookListKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "inputPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        setPageNumber();
                    }
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class BookListActionListener implements ActionListener {

        public BookListActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "confirm":
                    confirmDialog();
                    break;
                case "cancel":
                    dispose();
                    break;
                case "reset":
                    resetSearch();
                    break;
                case "filter":
                    filter.getOkButton().addActionListener(this);
                    showFilter();
                    break;
                case "filterConfirm":
                    setSilter();
                    break;
                case "search":
                    search();
                    break;
                case "prevPage":
                    prevPage();
                    break;
                case "nextPage":
                    nextPage();
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
    }
}
