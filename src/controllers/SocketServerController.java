/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import io.Configuration;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import remote.SocketServer;
import views.SocketServerDialog;

/**
 *
 * @author Nesh
 */
public class SocketServerController extends BaseController {

    private SocketServerDialog dialog;
    private static SocketServerController instance;
    private boolean isServerRunning = false;
    SocketServer s;

    public static SocketServerController getInstance() {
        synchronized (SocketServerController.class) {
            if (instance == null) {
                instance = new SocketServerController();
            } else {
            }
        }
        return instance;
    }

    private SocketServerController() {
        dialog = new SocketServerDialog(null, false);
        dialog.setLocation(200, 200);
        initListeners();
    }

    @Override
    void showView() {
        dialog.setVisible(true);
    }

    @Override
    void dispose() {
        dialog.setVisible(false);
    }

    private void initListeners() {
        SocketServerActionListener a = new SocketServerActionListener();
        dialog.getServerSwitchButton().addActionListener(a);
        dialog.getHideButton().addActionListener(a);
    }

    public void logMessage(String in) {
        dialog.getLogOutput().setText(dialog.getLogOutput().getText() + in + "\n");
    }

    public void setServerStatus(boolean online) {
        if (online) {
            s = new SocketServer(instance);
            dialog.getServerStatus().setText("SERVER ONLINE");
            dialog.getServerStatus().setForeground(Color.green);
            dialog.getServerSwitchButton().setText("Vypnout Server");
            logMessage("Server naslouchá na portu " + String.valueOf(Configuration.getInstance().getServerPort()));
            isServerRunning = true;

        } else {
            if (s == null) {
                return;
            }
            s.closeSocket();
            s = null;
            dialog.getServerStatus().setText("SERVER OFFLINE");
            dialog.getServerStatus().setForeground(Color.red);
            dialog.getServerSwitchButton().setText("Zapnout Server");
            logMessage("Server odpojen");
            isServerRunning = false;
        }
    }

    public void serverFail(String err) {
        logMessage(err);
        setServerStatus(false);
    }

    private class SocketServerActionListener implements ActionListener {

        public SocketServerActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "hideButton":
                    dialog.setVisible(false);
                    break;
                case "switchButton":
                    if (isServerRunning) {
                        setServerStatus(false);
                    } else {
                        setServerStatus(true);
                    }
                    break;
            }
        }
    }
}
