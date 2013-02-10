package models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Book;
import services.BookService;

public class BookTableModel extends AbstractTableModel {

    private List<Book> itemList;
    private boolean showTitle = true;
    private boolean showAuthor = true;
    private boolean showPublisher;
    private boolean showPublishedYear;
    private boolean showlanguage;
    private boolean showISBN10;
    private boolean showISBN13;
    private boolean showPageCount;
    private boolean showLocation;
    private boolean showItemCount;
    private int page;
    private int maxRows;

    public BookTableModel() {
        super();
        itemList = BookService.getInstance().getBooks();
        page = 1;
        maxRows = 10;
    }

    public BookTableModel(List<Book> in) {
        super();
        page = 1;
        maxRows = 10;
        itemList = in;
    }

    @Override
    public int getRowCount() {
        if (page * maxRows < itemList.size()) {
            return maxRows;
        } else {
            return itemList.size() % maxRows;
        }

    }

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
        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book i = itemList.get(rowIndex + getSkippedRowsCount());
        ArrayList<String> tempValues = new ArrayList<>();

        if (showTitle) {
            tempValues.add(i.getTitle());
        }
        if (showAuthor) {
            if (i.getMainAuthor() == null) {
                tempValues.add("Neznámý autor");
            } else {
                tempValues.add(i.getMainAuthor().toString());
            }
        }
        if (showPublisher) {
            tempValues.add(i.getPublisher());
        }
        if (showPublishedYear) {
            if (i.getPublishedYear() != null) {
                tempValues.add(String.valueOf(i.getPublishedYear().getYear()));
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
            tempValues.add(String.valueOf(i.getBorrowedCount()) + "/" + String.valueOf(i.getCount()));
        }
        if (showLocation) {
            tempValues.add(i.getLocation());
        }
        return tempValues.get(columnIndex);
    }

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
            tempValuesColumnNames.add("Vypůjčeno");
        }
        if (showLocation) {
            tempValuesColumnNames.add("Umístění");
        }

        return tempValuesColumnNames.get(column);
    }

    public void setVisibility(boolean showTitle, boolean showAuthor, boolean showPublisher, boolean showPublishedYear, boolean showlanguage, boolean showISBN10, boolean showISBN13, boolean showPageCount, boolean showItemCount, boolean showLocation) {
        this.showTitle = showTitle;
        this.showAuthor = showAuthor;
        this.showPublisher = showPublisher;
        this.showPublishedYear = showPublishedYear;
        this.showlanguage = showlanguage;
        this.showISBN10 = showISBN10;
        this.showISBN13 = showISBN13;
        this.showPageCount = showPageCount;
        this.showItemCount = showItemCount;
        this.showLocation = showLocation;
    }

    public void updateData() {
        itemList = BookService.getInstance().getBooks();
    }

    private int getSkippedRowsCount() {
        return (page - 1) * maxRows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || (page - 1) * maxRows > itemList.size()) {
            return;
        }

        this.page = page - 1;
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
        if (page * maxRows > itemList.size()) {
            return;
        }
        page++;
    }

    public void prevPage() {
        if (page - 1 <= 0) {
            return;
        }
        page--;
    }
}
