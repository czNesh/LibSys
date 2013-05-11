/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import io.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.SystemUser;
import services.SystemUserService;

/**
 *
 * @author Nesh
 */
public class SystemUserTableModel extends AbstractTableModel {

    private List<SystemUser> userList;

    public void updateData() {
        if (!Configuration.getInstance().isDeletedItemVisible()) {
            SystemUserService.getInstance().getParameters().put("deleted", false);
            SystemUserService.getInstance().setCondition("deleted = :deleted");
        }

        userList = SystemUserService.getInstance().getSystemUsers();
    }

    @Override
    public int getRowCount() {
        return userList.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        ArrayList<String> tempValuesColumnNames = new ArrayList<>();
        tempValuesColumnNames.add("Jméno");
        tempValuesColumnNames.add("login");
        tempValuesColumnNames.add("E-mail");
        tempValuesColumnNames.add("Správce");
        tempValuesColumnNames.add("Aktivní");
        return tempValuesColumnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SystemUser u = userList.get(rowIndex);
        ArrayList<String> tempValues = new ArrayList<>();
        tempValues.add(u.getFullName());
        tempValues.add(u.getLogin());
        tempValues.add(u.getEmail());
        if (u.isMaster()) {
            tempValues.add("ANO");
        } else {
            tempValues.add("NE");
        }
        if (u.isDeleted()) {
            tempValues.add("NE");
        } else {
            tempValues.add("ANO");
        }

        return tempValues.get(columnIndex);
    }

    public SystemUser getSystemUser(int selectedRow) {
        return userList.get(selectedRow);
    }
}
