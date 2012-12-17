/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Nesh
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

}
