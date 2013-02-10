/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
import models.dao.BaseDAO;
import models.entity.SystemUser;
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
    
}
