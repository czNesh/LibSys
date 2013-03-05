/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import services.SystemUserService;

/**
 *
 * @author eXtreme
 */
public class LibSysStart {

    public static void main(String[] args) {
       // PREPARE p = new PREPARE();
      //  p.fillDB();
        
        AppController c = AppController.getInstance();
        c.setLoggedUser(SystemUserService.getInstance().find(1L));
        c.showMainFrame();
        // c.go();
    }
}
