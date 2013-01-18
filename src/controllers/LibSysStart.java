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
    }
}
