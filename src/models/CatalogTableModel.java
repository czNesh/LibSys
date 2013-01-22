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

    public CatalogTableModel(List<CatalogItem> l) {
        super();
        itemList = l;
    }

    @Override
    public int getRowCount() {
        return itemList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CatalogItem i = itemList.get(rowIndex);
        Object[] values = new Object[]{i.getTitle(), i.getMainAuthor().toString(), i.getYear().getYear(), i.getISN()};
        return values[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        String[] columnNames = new String[]{"Titul", "Autor", "Rok", "ISBN"};
        return columnNames[column];
    }
}