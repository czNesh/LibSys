/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.prefs.Preferences;
import views.SettingsDialog;

/**
 *
 * @author Nesh
 */
public class SettingsController extends BaseController {

    Preferences prefs = Preferences.userNodeForPackage(this.getClass());

   SettingsDialog dialog;
    
    public SettingsController() {
        dialog = new SettingsDialog(null, true); 
      //  prefs.putInt("MAX_ROWS", 15);
        System.out.println(prefs.getInt("MAX_ROWS", 20));
    }
    
    private void initListeners(){
    
    }
    
    private void updateDate(){
        
    }

    @Override
    void showView() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
