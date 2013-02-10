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
import services.CustomerService;
import models.entity.Author;
import models.entity.Book;
import models.entity.Customer;
import models.entity.Genre;
import models.entity.GenreType;
import models.entity.SystemUser;
import helpers.DateFormater;

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
        b.setPublishedYear(new Date(1960-1900, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958-1900, 1, 1));
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
        b.setPublishedYear(new Date(1960-1900, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958-1900, 1, 1));
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
        b.setPublishedYear(new Date(1960-1900, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958-1900, 1, 1));
        save(b);


        b = new Book();
        b.setTitle("Záhada hlavolamu");
        b.setAuthors(authors);
        b.setMainAuthor(a);
        b.setCount(5);
        b.setBorrowedCount(2);
        b.setGenres(genres);
        b.setISBN10("3116856446");
        b.setISBN13("3115416856446");
        b.setLocation("D2");
        b.setPageCount(550);
        b.setPublishedYear(new Date(1960-1900, 1, 1));
        b.setSponsor("CVUT");
        b.setAddedDate(new Date(1958-1900, 1, 1));
        save(b);



        String tempAuthors;
        String[] autorsArray;
        List<Author> authorList;
        String autori;
        b = new Book();
        b.setTitle("Neue Kloppelspitzen in Bandspitzenart");
        autori = "G. Reden";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }






        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("1.3.2005", false));
        save(b);
        b = new Book();
        b.setTitle("Neumreme na sláme");
        autori = "V. Mihálik";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("19.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Neviditelní nepřátelé");
        autori = "B. Karger-Decker";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("23.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Nezdárný syn");
        autori = "R. Bornard";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("23.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Němá barikáda");
        autori = "J. Drda";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("19.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Kouzlo keramiky a porcelánu");
        autori = "A. Braunová";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("22.2.2002", false));
        save(b);
        b = new Book();
        b.setTitle("Follová Ivana Inventury if...");
        autori = "Follová;Zindelová";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(0);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("26.1.2009", false));
        save(b);
        b = new Book();
        b.setTitle("Formenwelt aus dem Naturreiche");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(0);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("29.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Fortune for Free and other pieces");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(8);
        b.setCount(8);
        b.setAddedDate(DateFormater.stringToDate("9.8.2006", false));
        save(b);
        b = new Book();
        b.setTitle("Wandteppiche des 20. Jahrhunderts");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("25.11.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Warhol Andy");
        autori = "K. Honnef";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("8.1.2007", false));
        save(b);
        b = new Book();
        b.setTitle("Washington National Gallery");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("5.8.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Wasservogel in Freiland und gemege Kolb");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("26.7.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Water - Colours");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("19.8.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Švabinský Max");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("22.11.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Švabinský Max");
        autori = "A. Matějček";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("13.11.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Švédské stoly aneb jací jsme");
        autori = "M. Vieweg";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("26.2.2001", false));
        save(b);
        b = new Book();
        b.setTitle("Švýcarská tapiserie - katalog");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("25.11.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Tadsch Mahal");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("23.8.1999", false));
        save(b);
        b = new Book();
        b.setTitle("Taikomoji dekoratyvine daile");
        autori = "Neznámý";
        tempAuthors = autori;
        autorsArray = tempAuthors.split(";");
        authorList = new ArrayList<>();

        for (int i = 0; i < autorsArray.length; i++) {
            // VYTVORI POTREBNE PROMENE
            Author tempAuthor = new Author();
            String fname = "";
            String lname;

            String[] foo = autorsArray[i].split(" ");

            for (int j = 0; j < foo.length - 1; j++) {
                fname += foo[j];
            }
            lname = foo[foo.length - 1];
            tempAuthor.setFirstName(fname);
            tempAuthor.setLastName(lname);
            authorList.add(tempAuthor);
        }

        b.setAuthors(authorList);
        b.setMainAuthor(authorList.get(0));
        b.setBorrowedCount(1);
        b.setCount(1);
        b.setAddedDate(DateFormater.stringToDate("5.10.1999", false));
        save(b);

        
        Customer c = new Customer();
        c.setSSN(418769447);
        c.setFirstName("Petr");
        c.setLastName("Hejhal");
        c.setStreet("Pařížská 19");
        c.setCity("Praha");
        c.setCountry("Česká republika");
        c.setEmail("petr.hejhal@centrum.cz");
        c.setPhone("+420721771459");
        c.setPostcode("11000");
        save(c);
        
        c = new Customer();
        c.setSSN(628771395);
        c.setFirstName("Jaroslav");
        c.setLastName("Medek");
        c.setStreet("Kdesi v Beroune 1");
        c.setCity("Beroun");
        c.setCountry("Česká republika");
        c.setEmail("medy75@seznam.cz");
        c.setPhone("+420605447984");
        c.setPostcode("15800");
        save(c);
        
        c = new Customer();
        c.setSSN(184978134);
        c.setFirstName("Radek");
        c.setLastName("Ježdík");
        c.setStreet("Pražského povstání 23");
        c.setCity("Praha");
        c.setCountry("Česká republika");
        c.setEmail("redhead@seznam.cz");
        c.setPhone("+420606456144");
        c.setPostcode("12000");
        save(c);


    }
}
