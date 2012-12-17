/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.entity.SystemUser;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import util.HibernateUtil;

/**
 *
 * @author Nesh
 */
class PREPARE {

    public void fillDB() {
        // PREPARE DATA  
        SystemUser su = new SystemUser();
        su.setFirstName("Petr");
        su.setLastName("Hejhal");
        su.setEmail("petr.hejhal@centrum.cz");
        su.setLogin("Nesh");
        su.setMaster(true);
        su.setPassword("temp");

        // STORE
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction t = s.beginTransaction();
        t.begin();
        s.save(su);
        t.commit();

    }
}
