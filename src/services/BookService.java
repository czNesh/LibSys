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

    private String getFreeBarcode() {
        while (true) {
            // vygeneruje SSN
            long timeSeed = System.nanoTime();
            double randSeed = Math.random() * 1000;
            long midSeed = (long) (timeSeed * randSeed);
            String s = "5" + midSeed;
            String subStr = s.substring(0, 9);
            int finalSeed = Integer.parseInt(subStr);

            // volné SSN ?
            getParameters().put("barcode", finalSeed);
            if (getUnique("barcode = :barcode") == null) {
                return String.valueOf(finalSeed);
            }
        }
    }

    public void saveBook(Book b) {
        b.setBarcode(getFreeBarcode());
        bookList.add(b);
        save(b);
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
}
