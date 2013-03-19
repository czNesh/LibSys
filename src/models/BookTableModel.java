package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Author;
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
    private int page = 1;
    private int maxRows = 50;

    public BookTableModel() {
        super();
        itemList = BookService.getInstance().getBooks();
    }

    public BookTableModel(List<Book> in) {
        super();
        itemList = in;
    }

    @Override
    public int getRowCount() {
        return itemList.size();
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
        Book i = itemList.get(rowIndex);
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
            int borrowed = BookService.getInstance().getBorrowed(i.getVolumeCode());
            int count = BookService.getInstance().getCount(i.getVolumeCode());

            tempValues.add(String.valueOf(count - borrowed) + "/" + String.valueOf(count));
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
            tempValuesColumnNames.add("Skladem");
        }
        if (showLocation) {
            tempValuesColumnNames.add("Umístění");
        }

        return tempValuesColumnNames.get(column);
    }

    public void setViewSettings(boolean showTitle, boolean showAuthor, boolean showPublisher, boolean showPublishedYear, boolean showlanguage, boolean showISBN10, boolean showISBN13, boolean showPageCount, boolean showItemCount, boolean showLocation) {
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || page > getTotalPageCount()) {
            return;
        }

        this.page = page;
        BookService.getInstance().setStart((page - 1) * maxRows);
        updateData();
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
        BookService.getInstance().setStart((page - 1) * maxRows);
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
        BookService.getInstance().setFilter(" GROUP BY volumeCode");
        return (int) Math.round((BookService.getInstance().getTotalCount() / maxRows) + 0.5); // round up
    }

    public Book getBook(int index) {
        return itemList.get(index);
    }

    public void setFilter(String barcode, String title, String author, String isbn10, String isbn13, Date year) {
        itemList = BookService.getInstance().getFilteredList(barcode, title, author, isbn10, isbn13, year);
    }

    public void resetFilter() {
        BookService.getInstance().resetFilter();
        BookService.getInstance().getParameters().clear();
        itemList = BookService.getInstance().getBooks();
    }

    public void applyFilter(String filterString) {
        itemList = BookService.getInstance().getFilteredResult(filterString);
    }
}
