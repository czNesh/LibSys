/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import models.entity.CatalogItem;
import org.hibernate.Session;

/**
 *
 * @author Nesh
 */
public class CatalogItemDAO extends BaseDAO<CatalogItem> implements Serializable {

    private static CatalogItemDAO instance;

    public static CatalogItemDAO getInstance() {
        synchronized (CatalogItemDAO.class) {
            if (instance == null) {
                instance = new CatalogItemDAO(null);
            }
        }
        return instance;
    }

    public CatalogItemDAO(Session session) {
        super(session);
    }
    
    public List<CatalogItem> getItems(){
        return getList();
    }
}
