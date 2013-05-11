/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
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
            if (Configuration.getInstance().isSkipLogging()) {
                c.setLoggedUser(SystemUserService.getInstance().getDefaultSystemUser());
                c.showMainFrame();
            } else {
                if (SystemUserService.getInstance().isOnlyDefault()) {
                    Configuration.getInstance().setSkipLogging(true);
                    c.setLoggedUser(SystemUserService.getInstance().getDefaultSystemUser());
                    c.showMainFrame();
                }
                c.go();
            }

        } catch (Exception ex) {
            System.out.println("CHYBA V PROGRAMU:");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
