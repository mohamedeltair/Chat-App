/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import importedpackage.Message;
import importedpackage.StringContent;

/**
 *
 * @author elteir
 */
public class AppKicker extends Thread {
    private String username;
    private Client client;

    public AppKicker(String username, Client client) {
        this.username = username;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public Client getClient() {
        return client;
    }
    
    public void run() {
        client.sendToServer(new Message(6, new StringContent(username)));
    }
}
