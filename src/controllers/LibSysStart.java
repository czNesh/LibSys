/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import services.SystemUserService;

/**
 *
 * @author eXtreme
 */
public class LibSysStart {

    public static void main(String[] args) {
        try {
            AppController c = AppController.getInstance();
            c.setLoggedUser(SystemUserService.getInstance().find(1L));
            c.showMainFrame();
            c.go();
            
        } catch (Exception ex) {
            Logger.getLogger(LibSysStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
