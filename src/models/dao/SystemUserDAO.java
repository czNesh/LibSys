/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.io.Serializable;
import models.entity.SystemUser;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public class SystemUserDAO extends BaseDAO<SystemUser> implements Serializable {

    private static SystemUserDAO instance;

    public static SystemUserDAO getInstance() {
        synchronized (SystemUserDAO.class) {
            if (instance == null) {
                instance = new SystemUserDAO(null);
            }
        }
        return instance;
    }

    public SystemUserDAO(Session session) {
        super(session);
    }

    public SystemUser login(String login, String password) {
        getParameters().put("login", login);
        getParameters().put("password", password);
        return getUnique("login = :login AND password = :password");
    }   
    
}
