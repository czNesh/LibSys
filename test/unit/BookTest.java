/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unit;

import helpers.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import models.entity.Author;
import models.entity.Book;
import models.entity.Genre;
import services.AuthorService;
import services.BookService;
import services.GenreService;

/**
 *
 * @author Nesh
 */
public class BookTest extends TestCase {

    private static void create() {
        // SERVICE
        GenreService gs = GenreService.getInstance();
        AuthorService as = AuthorService.getInstance();
        BookService bs = BookService.getInstance();

        // Příprava dat
        Date buy = DateHelper.getCurrentDate(false);
        Date publishedIn = DateHelper.stringToDate("2010", true);
        String ISBN = "1145548754";
        String ISBN13 = "9785548754113";
        String title = "Test kniha";
        String notes = "Tohle \n je \n poznámka";
        String location = "sklad";
        String publisher = "FEL CVUT";
        String sponsor = "CVUT";
        String language = "cs";
        int pageCount = 90;
        int count = 2;

        // Příprava autorů
        Author a = new Author("Petr", "Hejhal");
        Author b = new Author("Jan", "Nový");
        List<Author> authors = new ArrayList<>();
        authors.add(a);
        authors.add(b);

        // Příprava žánrů
        Genre g = new Genre();
        g.setGenre("Test");
        List<Genre> genres = new ArrayList<>();
        genres.add(g);

        // Uložení provázaných entit;
        gs.save(g);
        as.save(a);
        as.save(b);

        // Vytvoření knihy
        Book book = new Book();
        book.setAddedDate(buy);
        book.setPublishedYear(publishedIn);
        book.setAuthors(authors);
        book.setMainAuthor(a);
        book.setGenres(genres);
        book.setISBN10(ISBN);
        book.setISBN13(ISBN13);
        book.setTitle(title);
        book.setNotes(notes);
        book.setPageCount(pageCount);
        book.setPublisher(publisher);
        book.setSponsor(sponsor);
        book.setLanguage(language);
        book.setLocation(location);

        // Uložení knihy
        bs.saveBook(book, count);

        // TESTY
        assertEquals(title, book.getTitle());
        assertEquals(a, book.getMainAuthor());
        assertEquals(authors, book.getAuthors());
        assertEquals(publishedIn, book.getPublishedYear());
        assertEquals(buy, book.getAddedDate());
        assertEquals(ISBN, book.getISBN10());
        assertEquals(ISBN13, book.getISBN13());
        assertEquals(language, book.getLanguage());
        assertEquals(location, book.getLocation());
        assertEquals(notes, book.getNotes());
        assertEquals(pageCount, book.getPageCount());

        //TESTY GENEROVANYCH PARAMETRU
        System.out.println(book.getBarcode());
        assertTrue(book.getBarcode().matches("^5[0-9]{8}$"));
        assertFalse(book.isDeleted()); // nová kniha nesmí být ihned smazaná   
        assertEquals(null, book.getInventoriedDate()); // Neproběhla zatím žádná inventura
        assertEquals(count, bs.getBooksByVolumeCode(book.getVolumeCode(), false).size()); // shodný počet kusů
        
        System.out.println("TEST BYL USPESNY");
    }
}
