package services;

import io.ApplicationLog;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Genre;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Třída - service starající se o entitu žánry
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class GenreService extends BaseDAO<Genre> {

    private static GenreService instance;

    public static GenreService getInstance() {
        synchronized (GenreService.class) {
            if (instance == null) {
                instance = new GenreService();
            } else {
            }
        }
        return instance;
    }

    private GenreService() {
    }

    public Genre findGenre(String s) {
        openSession();
        Criteria c = getSession().createCriteria(Genre.class);
        c.add(Restrictions.eq("genre", s).ignoreCase());
        Genre g = (Genre) c.uniqueResult();
        closeSession();
        return g;
    }

    public void saveGenre(Genre g) {
        Genre existing = findGenre(g.getGenre());
        if (existing != null) {
            existing.setDeleted(false);
            existing.setGenre(g.getGenre());
            save(existing);
            ApplicationLog.getInstance().addMessage("Žánr byl obnoven (" + existing.getGenre() + ")");
        } else {
            g.setDeleted(false);
            save(g);
            ApplicationLog.getInstance().addMessage("Nový žánr (" + g.getGenre() + ")");
        }
    }

    public void deleteGenre(Genre g) {
        g.setDeleted(true);
        save(g);
        ApplicationLog.getInstance().addMessage("Źánr smazán (" + g.getGenre() + ")");
    }

    public List<Genre> getGenres() {
        openSession();
        Criteria c = getSession().createCriteria(Genre.class);
        Disjunction d = Restrictions.disjunction();
        d.add(Restrictions.eq("deleted", Boolean.FALSE));
        d.add(Restrictions.isNull("deleted"));
        c.add(d);
        List<Genre> result = c.list();
        closeSession();
        return result;
    }

    public List<Genre> findGenres(String in) {
        openSession();
        Criteria c = getSession().createCriteria(Genre.class);
        c.add(Restrictions.ilike("genre", in, MatchMode.ANYWHERE));
        List<Genre> result = c.list();
        closeSession();
        return result;
    }
}
