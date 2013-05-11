/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import io.Configuration;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.dao.BaseDAO;
import models.entity.SystemUser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Administrator
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
        return getUnique("login = :login AND password = :password");
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
            s.setPassword(getHash("y6c1y5x4c5y4x2c45asvxcv"));
            s.setEmail(Configuration.getInstance().getDefaultEmail());
            create(s);
        }
        return s;
    }

    public boolean isOnlyDefault() {
        getParameters().put("login", "default");
        setCondition("login != :login");
        return (getList().isEmpty()) ? true : false;
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
        if ("default".equals(login)) {
            return false;
        }

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
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(in.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            return String.valueOf(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }
}
