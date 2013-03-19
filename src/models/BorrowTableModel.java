/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import helpers.DateFormater;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Book;
import models.entity.Borrow;
import services.BookService;
import services.BorrowService;

/**
 *
 * @author Nesh
 */
public class BorrowTableModel extends AbstractTableModel {

    List<Borrow> borrowList;
    // View filter settings
    boolean showCustomer = true;
    boolean showItem = true;
    boolean showFrom = true;
    boolean showTo = true;
    boolean showReturned = true;
    boolean showLibrarian = false;
    //
    boolean userGrouping = true;
    // Paging settings
    int page = 1;
    int maxRows = 50;

    public BorrowTableModel() {
        super();
        borrowList = BorrowService.getInstance().getBorrows();
    }

    public BorrowTableModel(List<Borrow> in) {
        super();
        borrowList = in;
    }

    @Override
    public int getRowCount() {
        return borrowList.size();
    }

    @Override
    public int getColumnCount() {
        int i = 0;

        if (showCustomer) {
            i++;
        }
        if (showItem) {
            i++;
        }
        if (showFrom) {
            i++;
        }
        if (showTo) {
            i++;
        }
        if (showReturned) {
            i++;
        }
        if (showLibrarian) {
            i++;
        }
        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Borrow b = borrowList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();

        if (showCustomer) {
            tempValues.add(b.getCustomer().getFullName());
        }
        if (showItem) {
            StringBuilder books = new StringBuilder();
            for (Book temp : BorrowService.getInstance().getBooksOfBorrow(b.getBorrowCode())) {
                if (books.length() > 0) {
                    books.append("; ");
                }
                books.append(temp.getTitle());
            }

            tempValues.add(b.getItem().toString());
        }
        if (showFrom) {
            tempValues.add(DateFormater.dateToString(b.getFromDate(), false));
        }
        if (showTo) {
            tempValues.add(DateFormater.dateToString(b.getToDate(), false));
        }
        if (showReturned) {
            if (BorrowService.getInstance().isAllReturned(b.getBorrowCode())) {
                tempValues.add("VRÁCENO");
            } else {
                tempValues.add("NEVRÁCENO");
            }
        }
        if (showLibrarian) {
            tempValues.add(b.getLibrarian().toString());
        }

        return tempValues.get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();

        if (showCustomer) {
            tempValuesColumnNames.add("Zákazník");
        }
        if (showItem) {
            tempValuesColumnNames.add("Kniha");
        }
        if (showFrom) {
            tempValuesColumnNames.add("Od");
        }

        if (showTo) {
            tempValuesColumnNames.add("Do");
        }
        if (showReturned) {
            tempValuesColumnNames.add("Stav");
        }
        if (showLibrarian) {
            tempValuesColumnNames.add("Knihovník");
        }

        return tempValuesColumnNames.get(column);
    }

    public void updateData() {
        borrowList = BorrowService.getInstance().getBorrows();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || page > getTotalPageCount()) {
            return;
        }

        this.page = page;
        BorrowService.getInstance().setStart((page - 1) * maxRows);
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
        BorrowService.getInstance().setStart((page - 1) * maxRows);
        updateData();

    }

    public void prevPage() {
        if (page - 1 <= 0) {
            return;
        }
        page--;
        BorrowService.getInstance().setStart((page - 1) * maxRows);
        updateData();
    }

    public int getTotalPageCount() {
        BorrowService.getInstance().setFilter(" GROUP BY borrowCode");
        return (int) Math.round((BorrowService.getInstance().getTotalCount() / maxRows) + 0.5); // round up
    }

    public Borrow getBorrow(int index) {
        return borrowList.get(index);
    }

    public void setViewSettings(boolean showCustomer, boolean showItem, boolean showFrom, boolean showTo, boolean showReturned, boolean showLibrarian, boolean userGrouping) {
        this.showCustomer = showCustomer;
        this.showItem = showItem;
        this.showFrom = showFrom;
        this.showTo = showTo;
        this.showReturned = showReturned;
        this.showLibrarian = showLibrarian;
        this.userGrouping = userGrouping;
    }
}
