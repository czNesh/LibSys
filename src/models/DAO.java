/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Map;

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
}
