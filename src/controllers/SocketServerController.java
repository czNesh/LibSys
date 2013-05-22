package controllers;

import io.Configuration;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import remote.SocketServer;
import views.SocketServerDialog;

/**
 * Třída (controller) starající se o LibSys Server
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class SocketServerController extends BaseController {

    private SocketServerDialog dialog; // připojený pohled
    private static SocketServerController instance; // instance této třídy
    private boolean isServerRunning = false; // indikace běhu serveru
    SocketServer s; // server socket

    public static SocketServerController getInstance() {
        synchronized (SocketServerController.class) {
            if (instance == null) {
                instance = new SocketServerController();
            } else {
            }
        }
        return instance;
    }

    /**
     * Třídní konstruktor
     */
    private SocketServerController() {
        dialog = new SocketServerDialog(null, false);
        dialog.setLocation(200, 200);
        initListeners();
    }

    /**
     * Vycentrování a zobrazení pohledu
     */
    @Override
    void showView() {
        dialog.setVisible(true);
    }

    /**
     * Zrušení pohledu
     */
    @Override
    void dispose() {
        dialog.setVisible(false);
    }

    /**
     * Inicializace listenerů
     */
    private void initListeners() {
        SocketServerActionListener a = new SocketServerActionListener();
        dialog.getServerSwitchButton().addActionListener(a);
        dialog.getHideButton().addActionListener(a);
    }

    /**
     * Přidání zprávy do konzole serveru
     *
     * @param in zpráva
     */
    public void logMessage(String in) {
        dialog.getLogOutput().setText(dialog.getLogOutput().getText() + in + "\n");
    }

    /**
     * Vypnutí / zapnutí serveru
     *
     * @param online požadované nastavení
     */
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

    /**
     * Pád serveru se zprávou
     *
     * @param err zpráva
     */
    public void serverFail(String err) {
        logMessage(err);
        setServerStatus(false);
    }

    /**
     * Zapne / Vypne server
     */
    private void switchServerRunning() {
        if (isServerRunning) {
            setServerStatus(false);
        } else {
            setServerStatus(true);
        }
    }

    /**
     * Třída zodpovídající za akci z odposlouchávaných komponent pohledu
     */
    private class SocketServerActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (((JComponent) e.getSource()).getName()) {
                case "hideButton":
                    dialog.setVisible(false);
                    break;
                case "switchButton":
                    switchServerRunning();
                    break;
            }
        }
    }
}
