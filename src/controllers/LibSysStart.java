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
        
//        GoogleBooksSearch gbs = new GoogleBooksSearch();
//        gbs.setISBN("9788025115824");
//        gbs.Search();
//        System.out.println(gbs.getBestResult().getTitle());
    }
}
