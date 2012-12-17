/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.entity.SystemUser;

/**
 *
 * @author Nesh
 */
public class SystemUserModel extends BaseModel {

    private static SystemUserModel instance;
    SystemUser loggedUser;

    public static SystemUserModel getInstance() {
        synchronized (SystemUserModel.class) {
            if (instance == null) {
                instance = new SystemUserModel();
            }
        }
        return instance;
    }

    public boolean login(String login, String password) {
        SystemUser su = (SystemUser) getOneByDQL("from SystemUser WHERE login = ? AND password = ?", login, password);
        System.out.println("PRIHLASENI UZIVATELE: " + su.getFirstName()+" "+su.getLastName());
        loggedUser = su;
        return (su != null);
    }
}
