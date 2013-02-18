/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.Customer;
import services.CustomerService;

/**
 *
 * @author Nesh
 */
public class CustomerTableModel extends AbstractTableModel {

    private List<Customer> customersList;
    boolean showSSN;
    boolean showName;
    boolean showStreet;
    boolean showCity;
    boolean showCountry;
    boolean showEmail;
    boolean showPhone;
    boolean showNotes;

    public CustomerTableModel() {
        super();
        customersList = CustomerService.getInstance().getCustomers();
        showName = true;
        showCity = true;
    }

    @Override
    public int getRowCount() {
        return (customersList == null) ? 0 : customersList.size();
    }

    @Override
    public int getColumnCount() {
        int i = 0;
        if (showSSN) {
            i++;
        }
        if (showName) {
            i++;
        }
        if (showStreet) {
            i++;
        }
        if (showCity) {
            i++;
        }
        if (showCountry) {
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

    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();

        if (showSSN) {
            tempValuesColumnNames.add("Číslo uživatele");
        }
        if (showName) {
            tempValuesColumnNames.add("Jméno");
        }

        if (showStreet) {
            tempValuesColumnNames.add("Ulice");
        }
        if (showCity) {
            tempValuesColumnNames.add("Město");
        }
        if (showCountry) {
            tempValuesColumnNames.add("Stát");
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer c = customersList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();

        if (showSSN) {
            tempValues.add(String.valueOf(c.getSSN()));
        }
        if (showName) {
            tempValues.add(c.getFirstName() + " " + c.getLastName());
        }
        if (showStreet) {
            tempValues.add(c.getStreet());
        }
        if (showCity) {
            tempValues.add(c.getCity());
        }
        if (showCountry) {
            tempValues.add(c.getCountry());
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
        customersList = CustomerService.getInstance().getCustomers();
    }

    public void setVisibility(boolean showSSN, boolean showName, boolean showStreet, boolean showCity, boolean showCountry, boolean showEmail, boolean showPhone, boolean showNotes) {
        this.showSSN = showSSN;
        this.showName = showName;
        this.showStreet = showStreet;
        this.showCity = showCity;
        this.showCountry = showCountry;
        this.showEmail = showEmail;
        this.showPhone = showPhone;
        this.showNotes = showNotes;
    }

    public void setFilter(String ssn, String fname, String lname, String email, String phone) {
        customersList = CustomerService.getInstance().getFilteredList(ssn, fname, lname, email, phone);
    }

    public void removeFilter() {
        customersList = CustomerService.getInstance().getCustomers();
    }

    public Customer getCustomer(int i) {
        return customersList.get(i);
    }
}
