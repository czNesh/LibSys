/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 *
 * @author Nesh
 */
public class ApplicationLog implements EventListener{

    private static ApplicationLog instance;
    private List<String> logList;

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
    
    public void addMessage(String in){
        logList.add(in);
    }

    public List<String> getLog() {
        return logList;
    }

}
