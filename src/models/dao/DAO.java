/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Nesh
 */
public interface DAO<T> {

    void create(T object);

    void delete(T object);

    T find(Long id);

    void update(T object);
    
    void save(T object);
    
    List<T> getList();
    
    Transaction getTransaction();
    
    void setTransaction(Transaction transaction);
    
    Session getSession();
    
    void setSession(Session session);
    
    T getUnique(String condition);
}
