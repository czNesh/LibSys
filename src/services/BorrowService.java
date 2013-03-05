/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.AppController;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Book;
import models.entity.Borrow;

/**
 *
 * @author Nesh
 */
public class BorrowService extends BaseDAO<Borrow> {
    
    private static BorrowService instance;
    
    public static BorrowService getInstance() {
        synchronized (BorrowService.class) {
            if (instance == null) {
                instance = new BorrowService();
            }
        }
        return instance;
    }
    
    public void newBorrow(Borrow b, List<Book> booksList) {
        for (Book book : booksList) {
            b.setItem(book);
            b.setLibrarian(AppController.getInstance().getLoggedUser());
            create(b);
        }
    }

    public List<Borrow> getBorrows() {
        return getList();
    }

    public List<Borrow> getFilteredList() {
        return getList();
    }
}
