/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.dao.CustomerDAO;
import models.entity.Customer;

/**
 *
 * @author Nesh
 */
public class CustomerTableModel extends AbstractTableModel {

    private List<Customer> customersList;
    boolean showCID;
    boolean showName;
    boolean showStreet;
    boolean showCity;
    boolean showCountry;
    boolean showEmail;
    boolean showPhone;

    @Override
    public int getRowCount() {
        return (customersList == null) ? 0 : customersList.size();
    }

    public CustomerTableModel() {
        customersList = CustomerDAO.getInstance().getList();
        showName = true;
        showCity = true;
    }

    @Override
    public int getColumnCount() {
        int i = 0;
        if (showCID) {
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
        return i;
    }

    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();

        if (showCID) {
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

        return tempValuesColumnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer c = customersList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();

        if (showCID) {
            tempValues.add(String.valueOf(c.getUCID()));
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

        return tempValues.get(columnIndex);
    }

    public void updateData() {
        CustomerDAO.getInstance().resetList();
        customersList = CustomerDAO.getInstance().getList();
    }

    public void setVisibility(boolean showCID, boolean showName, boolean showStreet, boolean showCity, boolean showCountry, boolean showEmail, boolean showPhone) {
        this.showCID = showCID;
        this.showName = showName;
        this.showStreet = showStreet;
        this.showCity = showCity;
        this.showCountry = showCountry;
        this.showEmail = showEmail;
        this.showPhone = showPhone;
    }
}
