/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import models.entity.SystemUser;
import services.SystemUserService;

/**
 *
 * @author eXtreme
 */
public class LibSysStart {

    public static void main(String[] args) {


        try {
            AppController c = AppController.getInstance();
            SystemUser defaultUser = SystemUserService.getInstance().getDefaultSystemUser();
            if (Configuration.getInstance().isSkipLogging()) {
                c.setLoggedUser(defaultUser);
                c.showMainFrame();
            } else {
                c.go();
            }

        } catch (Exception ex) {
            System.out.println("CHYBA V PROGRAMU:");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
