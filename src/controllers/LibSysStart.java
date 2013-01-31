/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author eXtreme
 */
public class LibSysStart {

    public static void main(String[] args) {
        AppController c = AppController.getInstance();
        
        PREPARE p = new PREPARE();
        p.fillDB();
        
        c.go();
//        
//        GoogleBooksSearch gbs = new GoogleBooksSearch();
//        gbs.setTitle("Návrhové");
//        gbs.search();
//        ArrayList<Book> items = gbs.getResults();
//        System.out.println("Nalezeno: " + gbs.getResultsCount() + " knih");
//        
//        for (int i = 0; i < items.size(); i++) {
//            Book b = items.get(i);
//            System.out.println("KNIHA ----------------");
//            System.out.println("titul:" + b.getTitle());
//            ArrayList<Author> authors = b.getAuthors();
//            System.out.println("autori:");
//            for (int j = 0; j < authors.size(); j++) {
//                System.out.println(authors.get(j).toString());
//            }
//            System.out.println("isbn10: " + b.getISBN10());
//            System.out.println("isbn13: " + b.getISBN13());
//            System.out.println("vydavatel: " + b.getPublisher());
//            System.out.println("vydano: " + b.getPublishedYear().getYear());
//            System.out.println("stanek: " + b.getPageCount());
//            System.out.println("jazyk: " + b.getLanguage());
//            System.out.println("KNIHA ----------------");
//        }
    }
}
