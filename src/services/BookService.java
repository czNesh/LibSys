/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.RefreshController;
import helpers.DateFormater;
import io.ApplicationLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.dao.BaseDAO;
import models.entity.Author;
import models.entity.Book;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

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

    private BookService() {
        // SINGLETON
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
        RefreshController.getInstance().refreshBookTab();
        ApplicationLog.getInstance().addMessage("Uložení knih proběhlo úspěšně (" + count + "x " + b.getTitle() + ")");
    }

    public BookService(Session session) {
        super(session);
    }

    public List<Book> getBooks() {
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
        setGroupBy("volumeCode");

        return getList();
    }

    public int getBorrowed(String volumeCode) {
        openSession();
        int count = 0;
        try {
            count = ((Long) getSession().createQuery("SELECT SUM(b.borrowed) FROM Book b WHERE volumeCode = :volumeCode AND deleted = :deleted").setParameter("volumeCode", volumeCode).setParameter("deleted", false).uniqueResult()).intValue();
        } catch (NullPointerException e) {
            return 0;
        }

        closeSession();
        return count;
    }

    public int getCount(String volumeCode) {
        openSession();
        int count = ((Long) getSession().createQuery("SELECT COUNT(*) FROM Book WHERE volumeCode = :volumeCode AND deleted = :deleted").setParameter("volumeCode", volumeCode).setParameter("deleted", false).uniqueResult()).intValue();
        closeSession();
        return count;
    }

    public Book getBookWithCode(String code) {
        getParameters().put("barcode", code);
        return getUnique("barcode = :barcode");
    }

    public void delete(String code) {
        getParameters().put("barcode", code);
        Book b = getUnique("barcode = :barcode");
        b.setDeleted(true);
        update(b);
        ApplicationLog.getInstance().addMessage("Smazána kniha (" + b.getBarcode() + " - " + b.getTitle() + ")");
        RefreshController.getInstance().refreshBookTab();
    }

    public List<Book> getBooksByVolumeCode(String volumeCode, boolean selectDeletedItems) {

        getParameters().put("volumeCode", volumeCode);
        String conditionString = "volumeCode = :volumeCode";
        if (!selectDeletedItems) {
            getParameters().put("deleted", false);
            conditionString += " AND deleted = :deleted";
        }
        setCondition(conditionString);
        return getList();
    }

    public List<String> criteriaSearch(String in) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Book.class);

        Disjunction d = Restrictions.disjunction();
        d.add(Restrictions.ilike("title", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("publisher", in, MatchMode.ANYWHERE));
        d.add(Restrictions.in("mainAuthor", AuthorService.getInstance().findAuthors(in)));
        d.add(Restrictions.ilike("sponsor", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("notes", in, MatchMode.ANYWHERE));
        d.add(Restrictions.eq("barcode", in));
        d.add(Restrictions.eq("location", in).ignoreCase());
        d.add(Restrictions.eq("ISBN10", in));
        d.add(Restrictions.eq("ISBN13", in));
        c.add(d);

        c.setProjection(Projections.projectionList().add(Property.forName("barcode")));
        List<String> result = c.list();
        closeSession();
        return result;
    }

    public Map<String, String> getOrderByMap() {
        Map<String, String> out = new HashMap<>();
        out.put("ID", "id");
        out.put("Titul", "title");
        out.put("Datum přidání", "addedDate");
        out.put("Rok publikace", "publishedYear");
        out.put("Počet stránek", "pageCount");
        return out;
    }

    public List<String> extendedCriteriaSearch(String barcode, String title, String author, String bn10, String bn13, String year) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Book.class);

        Disjunction d = Restrictions.disjunction();
        boolean isRestrictionSet = false;
        if (title != null && !title.isEmpty()) {
            d.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
            isRestrictionSet = true;
        }
        if (author != null && !author.isEmpty()) {
            d.add(Restrictions.in("mainAuthor", AuthorService.getInstance().findAuthors(author)));
            isRestrictionSet = true;
        }
        if (year != null && !year.isEmpty()) {
            d.add(Restrictions.ilike("publishedYear", DateFormater.stringToDate(year, true)));
            isRestrictionSet = true;
        }
        if (barcode != null && !barcode.isEmpty()) {
            d.add(Restrictions.eq("barcode", barcode));
            isRestrictionSet = true;
        }
        if (bn10 != null && !bn10.isEmpty()) {
            d.add(Restrictions.eq("ISBN10", bn10));
            isRestrictionSet = true;
        }
        if (bn13 != null && !bn13.isEmpty()) {
            d.add(Restrictions.eq("ISBN13", bn13));
            isRestrictionSet = true;
        }
        if (!isRestrictionSet) {
            closeSession();
            return new ArrayList<>();
        }
        c.add(d);

        c.setProjection(Projections.projectionList().add(Property.forName("barcode")));
        List<String> result = c.list();

        closeSession();
        return result;
    }
}
