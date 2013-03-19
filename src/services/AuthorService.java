/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import models.dao.BaseDAO;
import models.entity.Author;

/**
 *
 * @author Nesh
 */
public class AuthorService extends BaseDAO<Author> implements Serializable {

    private static AuthorService instance;
    private List<Author> authors;

    public static AuthorService getInstance() {
        synchronized (AuthorService.class) {
            if (instance == null) {
                instance = new AuthorService();
            }
        }
        return instance;
    }

    private AuthorService() {
        authors = getList();
    }

    public Author getOrCreate(Author mainAuthor) {
        int index = authors.indexOf(mainAuthor);
        if (index == -1) {
            save(mainAuthor);
            authors.add(mainAuthor);
            return mainAuthor;
        } else {
            return authors.get(index);
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Author> findAuthors(String author) {
        List<Author> out = new ArrayList<>();
        for(Author a : authors){
            if(a.toString().contains(author)){
                out.add(a);
            }
        }
        return out;
    }
}
