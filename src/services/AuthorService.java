package services;

import java.io.Serializable;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Author;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Třída - service starající se o entitu autorů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class AuthorService extends BaseDAO<Author> implements Serializable {

    private static AuthorService instance;
    private List<Author> authors;

    public static AuthorService getInstance() {
        synchronized (AuthorService.class) {
            if (instance == null) {
                instance = new AuthorService();
            }
        }
        return instance;
    }

    private AuthorService() {
        authors = getList();
    }

    public Author getOrCreate(Author mainAuthor) {
        int index = authors.indexOf(mainAuthor);
        if (index == -1) {
            save(mainAuthor);
            authors.add(mainAuthor);
            return mainAuthor;
        } else {
            return authors.get(index);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Author> findAuthors(String author) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(Author.class);
        Disjunction d = Restrictions.disjunction();

        d.add(Restrictions.ilike("firstName", author, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("lastName", author, MatchMode.ANYWHERE));

        c.add(d);

        List<Author> result = c.list();

        closeSession();
        return result;
    }
}
