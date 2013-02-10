/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Book;
import org.hibernate.Session;


/**
 *
 * @author Nesh
 */
public class BookService extends BaseDAO<Book> implements Serializable {

    private static BookService instance;
    private List<Book> bookList;
    private AuthorService authorService;

    public static BookService getInstance() {
        synchronized (BookService.class) {
            if (instance == null) {
                instance = new BookService();
            }
        }
        return instance;
    }

    private BookService() {
        bookList = getList();
        authorService = AuthorService.getInstance();
    }

    public BookService(Session session) {
        super(session);
    }

    public List<Book> getBooks() {
        return bookList;
    }

    public void saveBook(Book b) {
        b.setMainAuthor(authorService.getOrCreate(b.getMainAuthor()));  
        bookList.add(b);
        save(b);
    }
}
