/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dao;

import java.io.Serializable;
import models.entity.Author;

/**
 *
 * @author Nesh
 */
public class AuthorDAO extends BaseDAO<Author> implements Serializable {

    private static AuthorDAO instance;

    public static AuthorDAO getInstance() {
        synchronized (AuthorDAO.class) {
            if (instance == null) {
                instance = new AuthorDAO();
            }
        }
        return instance;
    }

    private AuthorDAO() {
    }
    
    
}
