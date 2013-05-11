/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Nesh
 */
public class CodeReaderListener implements KeyListener {

    private StringBuilder input = new StringBuilder();
    private boolean isComplete = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // your code is scanned and you can access it using frame.getBarCode()
            // now clean the bar code so the next one can be read
     //       frame.setBarCode(new String());
        } else {
            // some character has been read, append it to your "barcode cache"
       //     frame.setBarCode(frame.getBarCode() + e.getKeyChar());
        }
    }
}
