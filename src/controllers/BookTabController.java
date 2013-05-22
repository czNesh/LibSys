package controllers;

import coding.Barcode;
import coding.QRCode;
import helpers.DateHelper;
import io.Configuration;
import io.FileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import models.BookTableModel;
import models.entity.Book;
import services.BookService;
import views.BookFilterDialog;
import views.BookSearchDialog;
import views.MainView;

/**
 * Třída (controller) starající se o záložku knih na hlavním pohledu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class BookTabController {

    private MainView mainView; // hlavní pohled
    private BookFilterDialog filter; // filtr zobrazení
    private BookTableModel tableModel; // model tabulky knih
    private BookSearchDialog bsd; // dialog pro vyhledávání
    // ŘAZENÍ
    Map<String, String> orderType = BookService.getInstance().getOrderTypeMap();
    Map<String, String> orderBy = BookService.getInstance().getOrderByMap();

    /**
     * Třídní konstruktor
     *
     * @param mainView hlavní pohled
     */
    public BookTabController(MainView mainView) {
        // MainView
        this.mainView = mainView;

        // TableModel
        tableModel = new BookTableModel();
        mainView.getCatalogTable().setModel(tableModel);

        // FilterDialog
        filter = new BookFilterDialog(mainView, true);
        setFilterData();

        // Book Search Dialog
        bsd = new BookSearchDialog(mainView, true);

        // INIT LISTENERS
        initListeners();

        // update view
        updateView();
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        // MouseListener
        mainView.getCatalogTable().addMouseListener(new BookTabMouseListener());

        // ActionListener
        BookTabActionListener a = new BookTabActionListener();
        mainView.getBookTableNextButton().addActionListener(a);
        mainView.getBookTablePrevButton().addActionListener(a);
        mainView.getFilterButton().addActionListener(a);
        mainView.getBTNsearch().addActionListener(a);
        mainView.getBTNstopBookSearch().addActionListener(a);
        mainView.getBarcodeButton().addActionListener(a);
        mainView.getQrcodeButton().addActionListener(a);

        // KeyListener
        BookTabKeyListener b = new BookTabKeyListener();
        mainView.getBookTableInputNumber().addKeyListener(b);
        mainView.getBookFilterInput().addKeyListener(b);

        // FILTER Listeners
        filter.getOkButton().addActionListener(a);

        // SEARCH Listener
        bsd.getSearchButton().addActionListener(a);
        bsd.getBTNcloseSearchDialog().addActionListener(a);
        bsd.getBTNreset().addActionListener(a);

    }

    /**
     * update dat pohledu
     */
    public void updateView() {
        // UPDATE DATA
        tableModel.updateData();

        // Update table
        tableModel.fireTableDataChanged();
        tableModel.fireTableStructureChanged();

        // Update page counting 
        mainView.getBookTableInputNumber().setText(String.valueOf(tableModel.getPage()));
        mainView.getBookTableTotalPage().setText("/ " + String.valueOf(tableModel.getTotalPageCount()));

        if (tableModel.getPage() == 1) {
            mainView.getBookTablePrevButton().setEnabled(false);
        } else {
            mainView.getBookTablePrevButton().setEnabled(true);
        }

        if (tableModel.getPage() == tableModel.getTotalPageCount()) {
            mainView.getBookTableNextButton().setEnabled(false);
        } else {
            mainView.getBookTableNextButton().setEnabled(true);
        }
    }

    /**
     * Nastavení filtru zobrazení
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

        updateView();
    }

    /**
     * Zobrazí filter
     */
    private void showFilter() {
        filter.setLocationRelativeTo(null);
        filter.setVisible(true);
    }

    /**
     * Zobrazí vyhledávání
     */
    private void searchDialog() {
        if (mainView.getTabPanel().getSelectedIndex() != 0) {
            return;
        }
        bsd.setLocationRelativeTo(null);
        bsd.setVisible(true);
    }

    /**
     * smaže vstupy vyhledávání
     */
    private void resetSearch() {
        bsd.getInputAuthor().setText("");
        bsd.getInputBarcode().setText("");
        bsd.getInputISBN10().setText("");
        bsd.getInputISBN13().setText("");
        bsd.getInputPublishedYear().setText("");
        bsd.getInputTitle().setText("");
    }

    /**
     * Zastaví vyhledávání
     */
    private void stopSearch() {
        bsd.getInputAuthor().setText("");
        bsd.getInputBarcode().setText("");
        bsd.getInputISBN10().setText("");
        bsd.getInputISBN13().setText("");
        bsd.getInputPublishedYear().setText("");
        bsd.getInputTitle().setText("");
        tableModel.stopSearch();
        updateView();
        mainView.getBTNstopBookSearch().setVisible(false);
    }

    /**
     * Vyhledá knihy
     */
    private void search() {
        tableModel.search(
                bsd.getInputBarcode().getText().trim(),
                bsd.getInputTitle().getText().trim(),
                bsd.getInputAuthor().getText().trim(),
                bsd.getInputISBN10().getText().trim(),
                bsd.getInputISBN13().getText().trim(),
                bsd.getInputPublishedYear().getText().trim());
        updateView();
        bsd.setVisible(false);

        mainView.getBTNstopBookSearch().setVisible(true);
    }

    /**
     * Nastaví filter
     */
    private void setFilter() {
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
        updateView();
        filter.setVisible(false);
    }

    /**
     * vygeneruje čárové kódy
     */
    private void generateBarcode() {
        if (mainView.getTabPanel().getSelectedIndex() != 0) {
            return;
        }
        if (mainView.getCatalogTable().getSelectedRowCount() > 0) {
            List<Book> books = tableModel.getBooks(mainView.getCatalogTable().getSelectedRows());
            String folderName = "ba-" + DateHelper.getCurrentDateIncludingTimeString();
            FileManager.getInstance().createDir(folderName);

            for (Book bx : books) {
                for (Book b : BookService.getInstance().getBooksByVolumeCode(bx.getVolumeCode(), false)) {
                    FileManager.getInstance().saveImage(folderName + "/" + b.getBarcode(), Barcode.encode(b.getBarcode()));
                }
            }
            FileManager.getInstance().open(folderName + "/");
        } else {
            JOptionPane.showMessageDialog(mainView, "Nejprve vyberte položky z tabulky", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * vygeneruje QR kódy
     */
    private void generateQRCode() {
        if (mainView.getTabPanel().getSelectedIndex() != 0) {
            return;
        }
        if (mainView.getCatalogTable().getSelectedRowCount() > 0) {
            List<Book> books = tableModel.getBooks(mainView.getCatalogTable().getSelectedRows());
            String folderName = "qr-" + DateHelper.getCurrentDateIncludingTimeString();
            FileManager.getInstance().createDir(folderName);

            for (Book bx : books) {
                for (Book b : BookService.getInstance().getBooksByVolumeCode(bx.getVolumeCode(), false)) {
                    FileManager.getInstance().saveImage(folderName + "/" + b.getBarcode(), QRCode.encode(b.getBarcode()));
                }
            }
            FileManager.getInstance().open(folderName + "/");
        } else {
            JOptionPane.showMessageDialog(mainView, "Nejprve vyberte položky z tabulky", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Třída zodpovídající za stisk klávesy z odposlouchávaných komponent
     * pohledu
     */
    private class BookTabKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "bookFilter":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (mainView.getBookFilterInput().getText().trim().isEmpty()) {
                            tableModel.applyFilter("");
                        } else {
                            tableModel.applyFilter(mainView.getBookFilterInput().getText().trim());
                        }
                        updateView();
                    }
                    break;
                case "bookPageNumber":
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String in = mainView.getBookTableInputNumber().getText();
                        try {
                            tableModel.setPage(Integer.parseInt(in));
                        } catch (NumberFormatException ex) {
                            System.out.println("NESPRAVNY FORMAT CISLA");
                        }
                        updateView();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class BookTabActionListener implements ActionListener {

        public BookTabActionListener() {
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
                    showFilter();
                    break;
                case "searchDialog":
                    searchDialog();
                    break;
                case "closeSearchDialog":
                    bsd.setVisible(false);
                    break;
                case "resetSearchDialog":
                    resetSearch();
                    break;
                case "stopSearch":
                    stopSearch();
                    break;
                case "search":
                    search();
                    break;
                case "filterConfirm":
                    setFilter();
                    break;
                case "barcode":
                    generateBarcode();
                    break;
                case "qrcode":
                    generateQRCode();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Třída zodpovídající za pohyby a akce myši z odposlouchávaných komponent
     * pohledu
     */
    private class BookTabMouseListener implements MouseListener {

        public BookTabMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Book b = (Book) tableModel.getBook(mainView.getCatalogTable().getSelectedRow());
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

    public BookTableModel getCatalogTableModel() {
        return tableModel;
    }
}
