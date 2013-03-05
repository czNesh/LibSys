/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.ApplicationLog;

/**
 *
 * @author Nesh
 */
public abstract class BaseController{

	/**
	 * Zobrazí pohled
	 */
	abstract void showView();


	/**
	 * Ukončí controller.
	 */
	abstract void dispose();
        
        /*
         * Přidá zprávu do logu
         */
        protected void updateLog(String message){
            ApplicationLog.getInstance().addMessage(message);
        }

}
