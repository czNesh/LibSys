package models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.dao.CatalogItemDAO;
import models.entity.CatalogItem;

public class CatalogTableModel extends AbstractTableModel {

    private List<CatalogItem> itemList;

    public CatalogTableModel() {
        super();
        itemList = CatalogItemDAO.getInstance().getList();
    }

    @Override
    public int getRowCount() {
        return itemList.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CatalogItem i = itemList.get(rowIndex);
        Object[] values = new Object[]{i.getId(), i.getTitle(), i.getMainAuthor(), i.getYear(), i.getLocation(), i.isBorrowed()};
        return values[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        String[] columnNames = new String[]{"id", "Titul", "Autor", "Rok", "Umístění","Půjčená?"};
        return columnNames[column];
    }
}