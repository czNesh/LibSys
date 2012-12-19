/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Set;
import javax.swing.table.AbstractTableModel;
import models.entity.CatalogItem;

/**
 *
 * @author Nesh
 */
public class CatalogTableModel extends AbstractTableModel {

    private Set<CatalogItem> items;

    public CatalogTableModel() {
        items = CatalogItemModel.getInstance().getAllItems();
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
