/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import controllers.SocketServerController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.entity.Book;
import services.BookService;

/**
 *
 * @author Nesh
 */
public class SocketServer extends Thread {

    SocketServerController controller;
    ServerSocket serverSocket = null;
    private int port = 6789;
    List<ServerThread> existingConnection;

    public SocketServer(SocketServerController controller) {
        this.controller = controller;
        existingConnection = new ArrayList<>();
        start();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (BindException e) {
            controller.serverFail("Port je využíván - nelze spustit");

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Odpojen");
            }
            if (!serverSocket.isClosed()) {
                ServerThread a = new ServerThread(socket);
                a.start();
                existingConnection.add(a);
            }
        }
    }

    public void closeSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }


            for (ServerThread s : existingConnection) {
                s.closeSocket();
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ServerThread extends Thread {

        protected Socket socket;
        Book b;

        public ServerThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        @Override
        public void run() {

            BufferedReader brinp = null;
            PrintWriter out = null;

            try {
                brinp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                return;
            }
            String line;
            while (!serverSocket.isClosed()) {

                try {
                    line = brinp.readLine();

                    if (line == null) {
                        continue;
                    }

                    if (line.startsWith("CODE")) {
                        b = BookService.getInstance().getBookWithCode(line.substring(4));
                        String toLog = "Přijímám kód (" + line.substring(4) + ")";
                        if (b != null) {
                            out.println("BOOK_FOUND");
                            toLog += " - Kniha: " + b.toString();
                        } else {
                            out.println("BOOK_NOT_FOUND");
                            toLog += " - TUTO KNIHU SYSTÉM NEEVIDUJE";
                        }
                        controller.logMessage(toLog);
                        continue;
                    }

                    if (line.startsWith("TITLE")) {
                        out.println(b.getTitle() + "");
                        continue;
                    }

                    if (line.startsWith("AUTHOR")) {
                        out.println(b.getMainAuthor().toString());
                        continue;
                    }

                    if (line.equals("END")) {
                        socket.close();
                        return;
                    }

                    controller.logMessage("Nerozumím požadavku klienta " + socket.getRemoteSocketAddress());
                    out.println("DONT_KNOW");
                } catch (SocketException e) {
                    System.out.println("Odpojen");
                    existingConnection.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                    existingConnection.remove(this);
                    return;
                }
            }
        }

        private void closeSocket() throws IOException {
            socket.close();
        }
    }
}
