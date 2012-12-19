/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.HashSet;
import java.util.Set;
import models.entity.CatalogItem;

/**
 *
 * @author Nesh
 */
public class CatalogItemModel extends BaseModel {

    private static CatalogItemModel instance;

    public static CatalogItemModel getInstance() {
        synchronized (CatalogItemModel.class) {
            if (instance == null) {
                instance = new CatalogItemModel();
            }
        }
        return instance;
    }

    public Set<CatalogItem> getAllItems() {
        Set<CatalogItem> temp = new HashSet<>();
        Set<Object> values = getAllbyDQL("from CatalogItem", (Object) null);

        for (Object o : values) {
            temp.add((CatalogItem) o);
        }
        return temp;
    }
}
