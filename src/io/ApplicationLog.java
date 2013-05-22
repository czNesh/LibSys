package io;

import controllers.BaseController;
import helpers.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Třída starající se o log aplikace
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class ApplicationLog {

    private static ApplicationLog instance; // instance této třídy
    private List<String> logList; // seznam zpráv
    private List<BaseController> listeners = new ArrayList<>();

    // SINGLETON
    public static ApplicationLog getInstance() {
        synchronized (ApplicationLog.class) {
            if (instance == null) {
                instance = new ApplicationLog();
            }
        }
        return instance;
    }

    /**
     * Třídní konsturktor
     */
    private ApplicationLog() {
        logList = new ArrayList<>();
    }

    /**
     * Přidá zprávu do logu
     *
     * @param in zpráva
     */
    public void addMessage(String in) {
        logList.add(DateHelper.dateToStringIncludingTime(new Date()) + ": " + in);

        // NOTIFY LISTENERS
        for (BaseController c : listeners) {
            c.logChanged();
        }
    }

    /**
     * Vrát seznam vech zpráv
     *
     * @return seznam zpráv
     */
    public List<String> getLog() {
        return logList;
    }

    /**
     * Vrátí poslední přidanou zprávu
     *
     * @return poslední přidaná zpráva
     */
    public String getLastLog() {
        return logList.get(logList.size() - 1);
    }

    /**
     * Registrace Listeneru
     *
     * @param c controller
     */
    public void registerListener(BaseController c) {
        listeners.add(c);
    }

    /**
     * Mazání Listeneru
     *
     * @param c controller
     */
    public void removeListener(BaseController c) {
        listeners.remove(c);
    }
}
