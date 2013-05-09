/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import helpers.DateHelper;
import io.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Book;
import models.entity.Borrow;
import services.BorrowService;

/**
 *
 * @author Nesh
 */
public class BorrowTableModel extends AbstractTableModel {

    List<Borrow> borrowList;
    boolean showCustomer;
    boolean showItems;
    boolean showFrom;
    boolean showTo;
    boolean showReturned;
    boolean showLibrarian;
    // Nastavení dotazu
    private int page = 1;
    private int maxRows = Configuration.getInstance().getMaxBorrowRowsCount();
    private String groupBy = "borrowCode";
    private List<String> resultOfSearch = new ArrayList<>();
    private String filterString = "";
    private boolean isSearching = false;

    


    public BorrowTableModel() {
        super();
        borrowList = BorrowService.getInstance().getBorrows();
    }

    public BorrowTableModel(List<Borrow> in) {
        super();
        setParams();
        borrowList = in;
    }

    private void setParams() {
        //Nastavení viditelnosti    
        showCustomer = Configuration.getInstance().isBorrowShowCustomer();
        showItems = Configuration.getInstance().isBorrowShowItems();
        showFrom = Configuration.getInstance().isBorrowShowFrom();
        showTo = Configuration.getInstance().isBorrowShowTo();
        showReturned = Configuration.getInstance().isBorrowShowReturned();
        showLibrarian = Configuration.getInstance().isBorrowShowLibrarian();

        maxRows = Configuration.getInstance().getMaxBorrowRowsCount();
        BorrowService.getInstance().setLimit(maxRows);
        BorrowService.getInstance().setStart((page - 1) * maxRows);
        BorrowService.getInstance().setGroupBy(groupBy);
        BorrowService.getInstance().setOrderType(Configuration.getInstance().getBorrowOrderType());
        BorrowService.getInstance().setOrderBy(Configuration.getInstance().getBorrowOrderBy());
        if (!Configuration.getInstance().isDeletedItemVisible()) {
            BorrowService.getInstance().getParameters().put("deleted", false);
            BorrowService.getInstance().setCondition("deleted = :deleted");
        }
        if (!filterString.isEmpty()) {
            BorrowService.getInstance().setExactMatch("t.id", BorrowService.getInstance().criteriaSearch(filterString));
        }
        if (!resultOfSearch.isEmpty()) {
            BorrowService.getInstance().setExactMatch("t.id", resultOfSearch);
        }
    }

    @Override
    public int getRowCount() {
        return (borrowList == null) ? 0 : borrowList.size();
    }

    @Override
    public int getColumnCount() {
        int i = 0;

        if (showCustomer) {
            i++;
        }
        if (showItems) {
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
        if (showItems) {
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
            tempValues.add(DateHelper.dateToString(b.getFromDate(), false));
        }
        if (showTo) {
            tempValues.add(DateHelper.dateToString(b.getToDate(), false));
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
        if (showItems) {
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
        setParams();
        if (isSearching && resultOfSearch.isEmpty()) {
            borrowList.clear();
            isSearching = false;
        } else {
            borrowList = BorrowService.getInstance().getBorrows();
        }
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
        setParams();
        return (int) Math.round((BorrowService.getInstance().getTotalCount() / maxRows) + 0.5); // round up
    }

    public Borrow getBorrow(int index) {
        return borrowList.get(index);
    }

    public void applyFilter(String filterString) {
        this.filterString = filterString;
    }

    public List<Borrow> getBorrows(int[] selectedRows) {
        List<Borrow> list = new ArrayList<>();
        for (int i = 0; i < selectedRows.length; i++) {
            list.add(borrowList.get(selectedRows[i]));
        }
        return list;
    }

    public void stopSearch() {
        resultOfSearch.clear();
        isSearching = false;
    }

    public void search(String borrowCode, String customer, String librarian, String item, String from, String to, int returned) {
        resultOfSearch.clear();
        resultOfSearch = BorrowService.getInstance().extendedCriteriaSearch(borrowCode, customer, librarian, item, from, to, returned);
        isSearching = true;
    }
}
