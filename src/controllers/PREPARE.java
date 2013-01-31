/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.dao.BaseDAO;
import models.entity.Author;
import models.entity.Book;
import models.entity.Genre;
import models.entity.GenreType;
import models.entity.SystemUser;

/**
 *
 * @author Nesh
 */
class PREPARE extends BaseDAO<Object> {

    public void fillDB() {

        // PREPARE DATA  
        SystemUser su = new SystemUser();
        su.setFirstName("Petr");
        su.setLastName("Hejhal");
        su.setEmail("petr.hejhal@centrum.cz");
        su.setLogin("Nesh");
        su.setMaster(true);
        su.setPassword("temp");

        save(su);

        Genre gFantasy = new Genre();
        gFantasy.setGenreType(GenreType.Fantasy);

        save(gFantasy);

        Genre gPohadka = new Genre();
        gPohadka.setGenreType(GenreType.Pohádka);

        save(gPohadka);

        Set<Genre> genres = new HashSet<>();
        genres.add(gFantasy);
        genres.add(gPohadka);

        Author a = new Author();
        a.setFirstName("Jaroslav");
        a.setLastName("Foglar");

        save(a);

        List<Author> authors = new ArrayList<>();
        authors.add(a);


        Book b = new Book();
        b.setTitle("Stínadla se bouří");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setCount(5);
        b.setBorrowedCount(2);
        b.setGenres(genres);
        b.setISBN10("3116856446");
        b.setISBN13("3115416856446");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setPublishedYear(new Date(1960, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958, 1, 1));
        save(b);
        
        b = new Book();
        b.setTitle("Stínadla se bouří");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setCount(5);
        b.setBorrowedCount(2);
        b.setGenres(genres);
        b.setISBN10("3116856446");
        b.setISBN13("3115416856446");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setPublishedYear(new Date(1960, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958, 1, 1));
        save(b);
        
        b = new Book();
        b.setTitle("Stínadla se bouří");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setCount(5);
        b.setBorrowedCount(2);
        b.setGenres(genres);
        b.setISBN10("3116856446");
        b.setISBN13("3115416856446");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setPublishedYear(new Date(1960, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958, 1, 1));
        save(b);
        
        
        b = new Book();
        b.setTitle("Stínadla se bouří");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setCount(5);
        b.setBorrowedCount(2);
        b.setGenres(genres);
        b.setISBN10("3116856446");
        b.setISBN13("3115416856446");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setPublishedYear(new Date(1960, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958, 1, 1));
        save(b);
        
        
    }
}
