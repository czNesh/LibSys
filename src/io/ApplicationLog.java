/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import controllers.BaseController;
import helpers.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Nesh
 */
public class ApplicationLog {

    private static ApplicationLog instance;
    private List<String> logList;
    private List<BaseController> listeners = new ArrayList<>();

    public static ApplicationLog getInstance() {
        synchronized (ApplicationLog.class) {
            if (instance == null) {
                instance = new ApplicationLog();
            }
        }
        return instance;
    }

    private ApplicationLog() {
        logList = new ArrayList<>();
    }

    public void addMessage(String in) {
        logList.add(DateHelper.dateToStringIncludingTime(new Date()) + ": "+ in);
        
        // NOTIFY LISTENERS
        for(BaseController c : listeners){
            c.logChanged();
        }
    }

    public List<String> getLog() {
        return logList;
    }

    public String getLastLog() {
        return logList.get(logList.size()-1);
    }

    public void registerListener(BaseController c) {
        listeners.add(c);
    }

    public void removeListener(BaseController c) {
        listeners.remove(c);
    }
}
