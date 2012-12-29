/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import models.entity.SystemUser;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public class SystemUserDAO extends BaseDAO<SystemUser> implements Serializable {

    public SystemUserDAO(Session session) {
        super(session);
    }

    public SystemUser validate(String loginName, String password) {
        getParameters().put("login", loginName);
        return getUnique("password = :password");
    }
}
