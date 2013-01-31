/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.io.Serializable;
import java.util.List;
import models.entity.Book;
import org.hibernate.Session;

/**
 *
 * @author Nesh
 */
public class BookDAO extends BaseDAO<Book> implements Serializable {

    private static BookDAO instance;

    public static BookDAO getInstance() {
        synchronized (BookDAO.class) {
            if (instance == null) {
                instance = new BookDAO(null);
            }
        }
        return instance;
    }

    public BookDAO(Session session) {
        super(session);
    }
    
    public List<Book> getItems(){
        return getList();
    }
}
