package models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.dao.CatalogItemDAO;
import models.entity.Book;

public class CatalogTableModel extends AbstractTableModel {

    private List<Book> itemList;

    public CatalogTableModel() {
        super();
        itemList = CatalogItemDAO.getInstance().getList();
    }

    public CatalogTableModel(List<Book> l) {
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
        Book i = itemList.get(rowIndex);
        Object[] values = new Object[]{i.getTitle(), i.getMainAuthor(), i.getPublishedYear().getYear(), i.getISBN10()};
        return values[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        String[] columnNames = new String[]{"Titul", "Autor", "Rok", "ISBN"};
        return columnNames[column];
    }
}