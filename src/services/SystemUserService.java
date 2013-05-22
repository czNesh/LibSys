package services;

import io.Configuration;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.SystemUser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Třída - service starající se o entitu uživatelů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class SystemUserService extends BaseDAO<SystemUser> implements Serializable {

    private static SystemUserService instance;

    public static SystemUserService getInstance() {
        synchronized (SystemUserService.class) {
            if (instance == null) {
                instance = new SystemUserService(null);
            }
        }
        return instance;
    }

    public SystemUserService(Session session) {
        super(session);
    }

    public SystemUser login(String login, String password) {
        getParameters().put("login", login);
        getParameters().put("password", getHash(password));
        getParameters().put("deleted", false);
        return getUnique("login = :login AND password = :password AND deleted = :deleted");
    }

    public SystemUser getDefaultSystemUser() {
        getParameters().put("default", "default");
        SystemUser s = getUnique("login = :default");
        if (s == null) {
            s = new SystemUser();
            s.setLogin("default");
            s.setMaster(true);
            s.setDeleted(false);
            s.setFirstName("Defaultní");
            s.setLastName("uživatel");
            s.setPassword(getHash("default"));
            s.setEmail(Configuration.getInstance().getDefaultEmail());
            create(s);
        }
        return s;
    }

    public List<SystemUser> findSystemUsers(String in) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(SystemUser.class);
        Disjunction d = Restrictions.disjunction();

        d.add(Restrictions.ilike("firstName", in, MatchMode.ANYWHERE));
        d.add(Restrictions.ilike("lastName", in, MatchMode.ANYWHERE));

        c.add(d);

        List<SystemUser> result = c.list();

        closeSession();
        return result;
    }

    public List<SystemUser> getSystemUsers() {
        return getList();
    }

    public boolean isSavePossible(String login) {
        openSession();
        Session s = getSession();
        Criteria c = s.createCriteria(SystemUser.class);
        c.add(Restrictions.eq("login", login).ignoreCase());
        SystemUser u = (SystemUser) c.uniqueResult();
        closeSession();
        return (u == null);
    }

    public String getHash(String in) {

        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");

            md.update(in.getBytes());
            byte[] mb = md.digest();
            String out = "";
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }

            return out;
        } catch (Exception ex) {
            return null;
        }

    }

    public void setDefaultEmail(String email) {
        getParameters().put("default", "default");
        SystemUser s = getUnique("login = :default");
        if (s != null) {
            s.setEmail(email);
            save(s);
        }
    }
}
