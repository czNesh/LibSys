/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import models.AbstractlModelDAO;
import models.entity.Author;
import models.entity.Book;
import models.entity.Genre;
import models.entity.GenreType;
import models.entity.SystemUser;

/**
 *
 * @author Nesh
 */
class PREPARE extends AbstractlModelDAO<Object> {

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

        Set<Author> authors = new HashSet<>();
        authors.add(a);


        Book b = new Book();
        b.setTitle("Stínadla se bouří");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setMinAge(-1);
        b.setBorrowed(false);
        b.setGenres(genres);
        b.setISN("ISN4449 4449 464465");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setYear(new Date(1960, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958, 1, 1));
        save(b);
    }
}
