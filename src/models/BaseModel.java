/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Nesh
 */
public abstract class BaseModel{

    Session s = HibernateUtil.getSessionFactory().openSession();
    Transaction t;

    protected void saveObject(Object in) {
        try {
            t = s.beginTransaction();
            t.setTimeout(5);
            s.saveOrUpdate(in);
            t.commit();
        } catch (RuntimeException ex) {
            System.out.println("CHYBA PRI UKLADANI" + in);
            t.rollback();
        }
    }
    
    protected Object getOneByIdx(Object o, int idx){
            Object temp = s.get(Object.class, idx);
            return temp;
    }
    
    protected Object getOneByDQL(String DQL, Object... params){
        // Vytvoří dotaz
        Query q = s.createQuery(DQL);
        
        // nastavení parametrů
        int i = 0;
        for(Object object : params){
            q.setParameter(i++, object);
        }
        
        // Vrátí výsledek dotazu
        return q.uniqueResult();
    }
    
    protected  HashSet<Object> getAllbyDQL(String DQL, Object... params){
        Set<Object> tempSet = new HashSet<>();
        // Vytvoří dotaz
        Query q = s.createQuery(DQL);
        
        // nastavení parametrů
        int i = 0;
        for(Object object : params){
            q.setParameter(i++, object);
        }
        
        // Vrátí výsledek dotazu
        return (HashSet<Object>) q.list();
    }
}
