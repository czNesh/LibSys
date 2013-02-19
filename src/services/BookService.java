/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import models.dao.BaseDAO;
import models.entity.Author;
import models.entity.Book;
import org.hibernate.Session;

/**
 *
 * @author Nesh
 */
public class BookService extends BaseDAO<Book> implements Serializable {

    private static BookService instance;
    private List<Book> bookList;

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
    }

    public BookService(Session session) {
        super(session);
    }

    public List<Book> getBooks() {
        return bookList;
    }

    public List<Book> getFilteredList(String barcode, String title, String author, String isbn10, String isbn13, Date year) {
        StringBuilder conditionStringBuilder = new StringBuilder();
        getParameters().clear();
        
        if (barcode != null && !barcode.isEmpty()) {
            conditionStringBuilder.append("barcode = :barcode");
            getParameters().put("barcode", barcode);
        }

        if (title != null && !title.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("title = :title");
            getParameters().put("title", title);
        }

        if (author != null && !author.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }

            List<Author> findedAuthors = AuthorService.getInstance().findAuthors(author);
            conditionStringBuilder.append("authors in :findedAuthors");
            getParameters().put("findedAuthors", findedAuthors);
        }

        if (isbn10 != null && !isbn10.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("isbn10 = :isbn10");
            getParameters().put("isbn10", isbn10);
        }

        if (isbn13 != null && !isbn13.isEmpty()) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("isbn13 = :isbn13");
            getParameters().put("isbn13", isbn13);
        }

        if (year != null) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" AND ");
            }
            conditionStringBuilder.append("publishedYear = :year");
            getParameters().put("year", year);
        }

        if (conditionStringBuilder.length() > 0) {
            setCondition(conditionStringBuilder.toString());
        }

        return getList();
    }
}
