/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.SystemUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author eXtreme
 */
public class LibSys {

    
    public static void main(String[] args) {
        
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        SystemUser u = new SystemUser();
        u.setLogin("Nesh");
        u.setEmail("petr.hejhal@mail.cz");
        u.setFirstName("Petr");
        u.setLastName("Hejhal");
        u.setMaster(true);
        u.setPassword("12345");
        
        Transaction t = session.getTransaction();
        t.begin();
        session.save(u);
        t.commit();
        session.close();
        
    }
}
