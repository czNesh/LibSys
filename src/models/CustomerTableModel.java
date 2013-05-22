package models;

import io.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Customer;
import services.CustomerService;

/**
 * Třída - model tabulky zákazníků
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class CustomerTableModel extends AbstractTableModel {

    private List<Customer> customerList;
    boolean showSSN;
    boolean showName;
    boolean showAdress;
    boolean showEmail;
    boolean showPhone;
    boolean showNotes;
    // Nastavení dotazu
    private int page = 1;
    private int maxRows = Configuration.getInstance().getMaxCustomerRowsCount();
    private List<String> resultOfSearch = new ArrayList<>();
    private String filterString = "";
    private boolean isSearching = false;

    /**
     * Defaultní konstruktor třídy
     */
    public CustomerTableModel() {
        super();
        setParams();
        customerList = CustomerService.getInstance().getCustomers();
    }

    /**
     * nastavení parametrů pro vyhledání
     */
    private void setParams() {
        //Nastavení viditelnosti    
        showSSN = Configuration.getInstance().isCustomerShowSSN();
        showName = Configuration.getInstance().isCustomerShowName();
        showAdress = Configuration.getInstance().isCustomerShowAdress();
        showEmail = Configuration.getInstance().isCustomerShowEmail();
        showPhone = Configuration.getInstance().isCustomerShowPhone();
        showNotes = Configuration.getInstance().isCustomerShowNotes();

        maxRows = Configuration.getInstance().getMaxCustomerRowsCount();
        CustomerService.getInstance().setLimit(maxRows);
        CustomerService.getInstance().setStart((page - 1) * maxRows);
        CustomerService.getInstance().setOrderType(Configuration.getInstance().getCustomerOrderType());
        CustomerService.getInstance().setOrderBy(Configuration.getInstance().getCustomerOrderBy());
        if (!Configuration.getInstance().isDeletedItemVisible()) {
            CustomerService.getInstance().getParameters().put("deleted", false);
            CustomerService.getInstance().setCondition("deleted = :deleted");
        }
        if (!filterString.isEmpty()) {
            CustomerService.getInstance().setExactMatch("t.id", CustomerService.getInstance().criteriaSearch(filterString));
        }
        if (!resultOfSearch.isEmpty()) {
            CustomerService.getInstance().setExactMatch("t.id", resultOfSearch);
        }
    }

    /**
     * Vrítí počet řádků
     *
     * @return počet řádků
     */
    @Override
    public int getRowCount() {
        return (customerList == null) ? 0 : customerList.size();
    }

    /**
     * Vrtí počet sloupců
     *
     * @return počet sloupců
     */
    @Override
    public int getColumnCount() {
        int i = 0;
        if (showSSN) {
            i++;
        }
        if (showName) {
            i++;
        }
        if (showAdress) {
            i++;
        }
        if (showEmail) {
            i++;
        }
        if (showPhone) {
            i++;
        }
        if (showNotes) {
            i++;
        }
        return i;
    }

    /**
     * Vrátí jméno sloupce
     *
     * @param column sloupec
     * @return jméno
     */
    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();

        if (showSSN) {
            tempValuesColumnNames.add("Číslo uživatele");
        }
        if (showName) {
            tempValuesColumnNames.add("Jméno");
        }

        if (showAdress) {
            tempValuesColumnNames.add("Adresa");
        }
        if (showEmail) {
            tempValuesColumnNames.add("E-mail");
        }
        if (showPhone) {
            tempValuesColumnNames.add("Telefon");
        }
        if (showNotes) {
            tempValuesColumnNames.add("Poznámky");
        }

        return tempValuesColumnNames.get(column);
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
        Customer c = customerList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();

        if (showSSN) {
            tempValues.add(String.valueOf(c.getSSN()));
        }
        if (showName) {
            tempValues.add(c.getFullName());
        }
        if (showAdress) {
            tempValues.add(c.getFullAdress());
        }

        if (showEmail) {
            tempValues.add(c.getEmail());
        }
        if (showPhone) {
            tempValues.add(c.getPhone());
        }
        if (showNotes) {
            tempValues.add(c.getNotes());
        }

        return tempValues.get(columnIndex);
    }

    public void updateData() {
        setParams();
        if (isSearching && resultOfSearch.isEmpty()) {
            customerList.clear();
            isSearching = false;
        } else {
            customerList = CustomerService.getInstance().getCustomers();
        }
    }

    public Customer getCustomer(int i) {
        return customerList.get(i);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || page > getTotalPageCount()) {
            return;
        }

        this.page = page;
        CustomerService.getInstance().setStart((page - 1) * maxRows);
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
        CustomerService.getInstance().setStart((page - 1) * maxRows);
        updateData();

    }

    public void prevPage() {
        if (page - 1 <= 0) {
            return;
        }
        page--;
        CustomerService.getInstance().setStart((page - 1) * maxRows);
        updateData();
    }

    public int getTotalPageCount() {
        setParams();
        return (int) Math.round((CustomerService.getInstance().getTotalCount() / maxRows) + 0.5); // round up
    }

    public List<Customer> getCustomers(int[] selectedRows) {
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < selectedRows.length; i++) {
            list.add(customerList.get(selectedRows[i]));
        }
        return list;
    }

    public void search(String ssn, String fname, String lname, String email, String phone) {
        resultOfSearch.clear();
        resultOfSearch = CustomerService.getInstance().extendedCriteriaSearch(ssn, fname, lname, email, phone);
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
}
