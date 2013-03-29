/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

/**
 *
 * @author Nesh
 */
public class RESTServer extends Thread {

    private static RESTServer instance;
   
    public static RESTServer getInstance() {
        synchronized (RESTServer.class) {
            if (instance == null) {
                instance = new RESTServer();
            }
        }
        return instance;
    }
    
    
}
