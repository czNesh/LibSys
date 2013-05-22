package models;

import io.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.entity.SystemUser;
import services.SystemUserService;

/**
 * Třída - model tabulky uživatelů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
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

    /**
     * Vrítí počet řádků
     *
     * @return počet řádků
     */
    @Override
    public int getRowCount() {
        return userList.size();
    }

    /**
     * Vrtí počet sloupců
     *
     * @return počet sloupců
     */
    @Override
    public int getColumnCount() {
        return 5;
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
        tempValuesColumnNames.add("Jméno");
        tempValuesColumnNames.add("login");
        tempValuesColumnNames.add("E-mail");
        tempValuesColumnNames.add("Správce");
        tempValuesColumnNames.add("Aktivní");
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
