/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import io.Configuration;
import java.io.Serializable;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.SystemUser;
import org.hibernate.Query;
import org.hibernate.Session;

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
        getParameters().put("password", password);
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
            s.setPassword("y6c1y5x4c5y4x2c45asvxcv");
            s.setEmail(Configuration.getInstance().getDefaultEmail());
            create(s);
        }
        return s;
    }

}
