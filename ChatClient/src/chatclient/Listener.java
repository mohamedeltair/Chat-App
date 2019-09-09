/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.*;
import java.net.*;
import importedpackage.*;

/**
 *
 * @author elteir
 */
public class Listener extends Thread {
    private ServerSocket ss;
    private Client client;
    
    public Listener(Client client) {
        this.client = client;
        try {
            ss = new ServerSocket(0);
        } catch (IOException ex) {

        }
    }
    
    public void run() {
        while(true) {
            try {
                Socket otherClient = ss.accept();
                Reader r = new Reader(otherClient, client);
                r.start();
            } catch (IOException ex) {

            }
        }
    }
    
    public SocketData getListenerData() {
        return new SocketData(ss.getInetAddress().getHostAddress(), ss.getLocalPort());
    }
}
