package controllers;

import io.Configuration;
import models.entity.SystemUser;
import services.SystemUserService;

/**
 * Spoštěcí třída
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class LibSysStart {

    /**
     * Start aplikace
     * @param args parametry 
     */
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
            System.err.print("CHYBA V PROGRAMU:" + ex.getLocalizedMessage());
            System.exit(-1); // "vybublá-li" výjimka až sem - skonči
        }
    }
}
