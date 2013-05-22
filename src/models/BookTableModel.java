package models;

import helpers.DateHelper;
import io.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Author;
import models.entity.Book;
import services.BookService;
import services.BorrowService;

public class BookTableModel extends AbstractTableModel {

    private List<Book> bookList;
    private boolean showTitle;
    private boolean showAuthor;
    private boolean showPublisher;
    private boolean showPublishedYear;
    private boolean showlanguage;
    private boolean showISBN10;
    private boolean showISBN13;
    private boolean showPageCount;
    private boolean showLocation;
    private boolean showItemCount;
    private String borrowCode = null;
    // Nastavení dotazu
    private int page = 1;
    private int maxRows = Configuration.getInstance().getMaxBookRowsCount();
    private String groupBy = "volumeCode";
    private List<String> resultOfSearch = new ArrayList<>();
    private String filterString = "";
    private boolean isSearching = false;
    private boolean forBorrow = false;

    /**
     * Defaultní konstruktor třídy
     */
    public BookTableModel() {
        super();
        setParams();
        bookList = BookService.getInstance().getBooks();
    }

    /**
     * Konstruktor třídy a naplnění dat z listu
     *
     * @param in list
     */
    public BookTableModel(List<Book> in) {
        super();
        showTitle = true;
        showAuthor = true;
        showPublishedYear = true;
        showISBN10 = true;
        showISBN13 = true;
        bookList = in;
    }

    /**
     * Konstruktor třídy a naplnění dat z listu
     *
     * @param in list
     * @param borrowCode kód
     */
    public BookTableModel(List<Book> in, String borrowCode) {
        super();
        showTitle = true;
        showAuthor = true;
        showPublishedYear = true;
        showISBN10 = true;
        showISBN13 = true;
        this.borrowCode = borrowCode;
        bookList = in;
    }

    /**
     * nastavení parametrů pro vyhledání
     */
    private void setParams() {
        //Nastavení viditelnosti  
        showTitle = Configuration.getInstance().isShowTitle();
        showAuthor = Configuration.getInstance().isShowAuthor();
        showPublisher = Configuration.getInstance().isShowPublisher();
        showPublishedYear = Configuration.getInstance().isShowPublishedYear();
        showlanguage = Configuration.getInstance().isShowLanguage();
        showISBN10 = Configuration.getInstance().isShowISBN10();
        showISBN13 = Configuration.getInstance().isShowISBN13();
        showPageCount = Configuration.getInstance().isShowPageCount();
        showLocation = Configuration.getInstance().isShowLocation();
        showItemCount = Configuration.getInstance().isShowCount();

        maxRows = Configuration.getInstance().getMaxBookRowsCount();
        BookService.getInstance().setLimit(maxRows);
        BookService.getInstance().setStart((page - 1) * maxRows);

        BookService.getInstance().setOrderType(Configuration.getInstance().getBookOrderType());
        BookService.getInstance().setOrderBy(Configuration.getInstance().getBookOrderBy());


        if (forBorrow) {
            BookService.getInstance().getParameters().put("borrowed", false);
            if (!Configuration.getInstance().isDeletedItemVisible()) {
                BookService.getInstance().getParameters().put("deleted", false);
                BookService.getInstance().setCondition("deleted = :deleted AND borrowed = :borrowed");
            } else {
                BookService.getInstance().setCondition("borrowed = :borrowed");
            }
        } else {
            BookService.getInstance().setGroupBy(groupBy);
            if (!Configuration.getInstance().isDeletedItemVisible()) {
                BookService.getInstance().getParameters().put("deleted", false);
                BookService.getInstance().setCondition("deleted = :deleted");
            }
        }
        if (!filterString.isEmpty()) {
            BookService.getInstance().setExactMatch("t.id", BookService.getInstance().criteriaSearch(filterString));
        }
        if (!resultOfSearch.isEmpty()) {
            BookService.getInstance().setExactMatch("t.id", resultOfSearch);
        }
    }

    /**
     * Vrítí počet řádků
     *
     * @return počet řádků
     */
    @Override
    public int getRowCount() {
        return bookList.size();
    }

    /**
     * Vrtí počet sloupců
     *
     * @return počet sloupců
     */
    @Override
    public int getColumnCount() {
        int i = 0;

        if (showTitle) {
            i++;
        }
        if (showAuthor) {
            i++;
        }
        if (showPublisher) {
            i++;
        }
        if (showPublishedYear) {
            i++;
        }
        if (showlanguage) {
            i++;
        }
        if (showISBN10) {
            i++;
        }
        if (showISBN13) {
            i++;
        }
        if (showPageCount) {
            i++;
        }
        if (showItemCount) {
            i++;
        }
        if (showLocation) {
            i++;
        }

        if (borrowCode != null) {
            i++;
        }
        return i;
    }

    /**
     * Vrátí hodnotu na řádku a sloupci
     *
     * @param rowIndex řádek
     * @param columnIndex sloupec
     * @return hodnota
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book i = bookList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();

        if (showTitle) {
            tempValues.add(i.getTitle());
        }
        if (showAuthor) {
            if (i.getMainAuthor() == null) {
                tempValues.add("Neznámý autor");
            } else {
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (Author a : i.getAuthors()) {
                    if (first) {
                        sb.append(a.toString());
                        first = false;
                        continue;
                    }
                    sb.append(("; " + a.toString()));

                }
                tempValues.add(sb.toString());
            }
        }
        if (showPublisher) {
            tempValues.add(i.getPublisher());
        }
        if (showPublishedYear) {
            if (i.getPublishedYear() != null) {
                tempValues.add(String.valueOf(DateHelper.dateToString(i.getPublishedYear(), true)));
            } else {
                tempValues.add("údaj chybí");
            }
        }
        if (showlanguage) {
            tempValues.add(i.getLanguage());
        }
        if (showISBN10) {
            tempValues.add(i.getISBN10());
        }
        if (showISBN13) {
            tempValues.add(i.getISBN13());
        }
        if (showPageCount) {
            tempValues.add(String.valueOf(i.getPageCount()));
        }
        if (showItemCount) {
            int borrowed = BookService.getInstance().getBorrowed(i.getVolumeCode());
            int count = BookService.getInstance().getCount(i.getVolumeCode());

            tempValues.add(String.valueOf(count - borrowed) + "/" + String.valueOf(count));
        }
        if (showLocation) {
            tempValues.add(i.getLocation());
        }

        if (borrowCode != null) {
            if (BorrowService.getInstance().isBookReturned(i, borrowCode)) {
                tempValues.add("NEVRÁCENO");
            } else {
                tempValues.add("VRÁCENO");
            }
        }
        return tempValues.get(columnIndex);
    }

    /**
     * Vrátí jméno sloupce
     * @param column sloupec
     * @return jméno
     */
    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();

        if (showTitle) {
            tempValuesColumnNames.add("Název");
        }
        if (showAuthor) {
            tempValuesColumnNames.add("Autor");
        }

        if (showPublisher) {
            tempValuesColumnNames.add("Vydavatel");
        }
        if (showPublishedYear) {
            tempValuesColumnNames.add("Rok vydání");
        }
        if (showlanguage) {
            tempValuesColumnNames.add("Jazyk");
        }
        if (showISBN10) {
            tempValuesColumnNames.add("ISBN 10");
        }
        if (showISBN13) {
            tempValuesColumnNames.add("ISBN 13");
        }
        if (showPageCount) {
            tempValuesColumnNames.add("Počet stránek");
        }
        if (showItemCount) {
            tempValuesColumnNames.add("Skladem");
        }
        if (showLocation) {
            tempValuesColumnNames.add("Umístění");
        }

        if (borrowCode != null) {
            tempValuesColumnNames.add("Stav");
        }

        return tempValuesColumnNames.get(column);
    }

    public void updateData() {
        setParams();
        if (isSearching && resultOfSearch.isEmpty()) {
            bookList.clear();
            isSearching = false;
        } else {
            bookList = BookService.getInstance().getBooks();
        }
    }

    public int getPage() {
        if (page > getTotalPageCount()) {
            page = getTotalPageCount();
        }
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || page > getTotalPageCount()) {
            return;
        }

        this.page = page;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        if (maxRows > 0) {
            this.maxRows = maxRows;
        }
    }

    public void nextPage() {
        if (page + 1 > getTotalPageCount()) {
            return;
        }
        page++;
        updateData();
    }

    public void prevPage() {
        if (page - 1 <= 0) {
            return;
        }
        page--;
        BookService.getInstance().setStart((page - 1) * maxRows);
        updateData();
    }

    public int getTotalPageCount() {
        setParams();
        return (int) Math.round((BookService.getInstance().getTotalCount() / maxRows) + 0.5); // round up
    }

    public Book getBook(int index) {
        return bookList.get(index);
    }

    public void search(String barcode, String title, String author, String isbn10, String isbn13, String year) {
        resultOfSearch.clear();
        resultOfSearch = BookService.getInstance().extendedCriteriaSearch(barcode, title, author, isbn10, isbn13, year);
        isSearching = true;
    }

    public void stopSearch() {
        resultOfSearch.clear();
        isSearching = false;
    }

    public void applyFilter(String filterString) {
        this.filterString = filterString;
    }

    public void stopSearchWithEmptyResult() {
        isSearching = false;
    }

    public List<Book> getBooks(int[] selectedRows) {
        List<Book> list = new ArrayList<>();
        for (int i = 0; i < selectedRows.length; i++) {
            list.add(bookList.get(selectedRows[i]));
        }
        return list;
    }

    public void setForBorrow(boolean in) {
        this.forBorrow = in;
    }
}
