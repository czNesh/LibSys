/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import helpers.DateFormater;
import io.ApplicationLog;
import io.Refresh;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
            getParameters().clear();
            getParameters().put("barcode", finalSeed);
            if (getUnique("barcode = :barcode") == null) {
                return String.valueOf(finalSeed);
            }
        }
    }

    private String getFreeVolumeCode() {
        while (true) {
            // vygeneruje SSN
            long timeSeed = System.nanoTime();
            double randSeed = Math.random() * 1000;
            long midSeed = (long) (timeSeed * randSeed);
            String s = "9" + midSeed;
            String subStr = s.substring(0, 9);
            int finalSeed = Integer.parseInt(subStr);

            // volné SSN ?
            getParameters().clear();
            getParameters().put("volumeCode", finalSeed);
            if (getUnique("volumeCode = :volumeCode") == null) {
                return String.valueOf(finalSeed);
            }
        }
    }

    public void saveBook(Book b, int count) {
        b.setVolumeCode(getFreeVolumeCode());
        b.setBorrowed(0);
        b.setDeleted(false);
        for (int i = 0; i < count; i++) {
            b.setBarcode(getFreeBarcode());
            create(b);
        }
        Refresh.getInstance().refreshBookTab();
        ApplicationLog.getInstance().addMessage("Uložení knih(y) proběhlo úspěšně (" + count + ")");
    }

    private BookService() {
    }

    public BookService(Session session) {
        super(session);
    }

    public List<Book> getBooks() {
        setFilter("GROUP BY volumeCode");
        return getList();
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
        setFilter("GROUP BY volumeCode");
        return getList();
    }

    public int getBorrowed(String volumeCode) {
        openSession();
        int count = ((Long) getSession().createQuery("SELECT SUM(b.borrowed) FROM Book b WHERE volumeCode = :volumeCode").setParameter("volumeCode", volumeCode).uniqueResult()).intValue();
        closeSession();
        return count;
    }

    public int getCount(String volumeCode) {
        openSession();
        int count = ((Long) getSession().createQuery("SELECT COUNT(*) FROM Book WHERE volumeCode = :volumeCode").setParameter("volumeCode", volumeCode).uniqueResult()).intValue();
        closeSession();
        return count;
    }

    public List<Book> getFilteredResult(String filterString) {
        StringBuilder conditionStringBuilder = new StringBuilder();
        getParameters().clear();

//        conditionStringBuilder.append("barcode = :filterString");
//        getParameters().put("filterString", filterString);
//
//        if (conditionStringBuilder.length() > 0) {
//            conditionStringBuilder.append(" OR ");
//        }
//        conditionStringBuilder.append("title LIKE :filterString");
//        getParameters().put("filterString", "%"+filterString+"%");
//


        List<Author> findedAuthors = AuthorService.getInstance().findAuthors(filterString);
        if (findedAuthors != null) {
            if (conditionStringBuilder.length() > 0) {
                conditionStringBuilder.append(" OR ");
            }
            conditionStringBuilder.append("authors in :findedAuthors");
            getParameters().put("findedAuthors", findedAuthors);
        }

//        if (conditionStringBuilder.length() > 0) {
//            conditionStringBuilder.append(" OR ");
//        }
//        conditionStringBuilder.append("isbn10 = :filterString");
//        getParameters().put("filterString", filterString);
//
//        if (conditionStringBuilder.length() > 0) {
//            conditionStringBuilder.append(" OR ");
//        }
//        conditionStringBuilder.append("isbn13 = :filterString");
//        getParameters().put("filterString", filterString);
//
//
//
//        if (conditionStringBuilder.length() > 0) {
//            conditionStringBuilder.append(" OR ");
//        }
//        conditionStringBuilder.append("publishedYear = :filterString");
//        getParameters().put("filterString", DateFormater.stringToDate(filterString, false));
//

        if (conditionStringBuilder.length() > 0) {
            setCondition(conditionStringBuilder.toString());
        }
        setFilter("GROUP BY volumeCode");
        return getList();
    }

    public Book getBookWithCode(String code) {
        getParameters().clear();
        getParameters().put("barcode", code);
        return getUnique("barcode = :barcode");
    }

    public void delete(String barcode) {
        // TODO
        System.out.println("MAZU");
    }
}
