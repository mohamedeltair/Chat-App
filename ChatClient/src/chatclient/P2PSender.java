/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import importedpackage.Message;
import importedpackage.P2PMessage;
import importedpackage.SocketData;

/**
 *
 * @author elteir
 */
public class P2PSender extends Thread {
    private SocketData sd1, sd2;
    private String message;
    private Client client;

    public P2PSender(SocketData sd1, SocketData sd2, String message, Client client) {
        this.sd1 = sd1;
        this.sd2 = sd2;
        this.message = message;
        this.client = client;
    }    

    public void run() {
        client.findSender(sd1, new Message(3, new P2PMessage(sd2, message)));
    }
}
