package controllers;

import io.ApplicationLog;

/**
 * Třída (controller) Rodič controllerů
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public abstract class BaseController {

    /**
     * Zobrazí pohled
     */
    abstract void showView();

    /**
     * Ukončí controller.
     */
    abstract void dispose();

    /**
     * Přidá zprávu do logu
     *
     * @param message text zprávy
     */
    protected void updateLog(String message) {
        ApplicationLog.getInstance().addMessage(message);
    }

    /**
     * Informuje controllery o změně logu
     */
    public void logChanged() {
        // STANDARDNE - nedelej nic
    }
}
