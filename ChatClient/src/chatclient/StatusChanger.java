/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import importedpackage.Message;
import importedpackage.StrNames;
import java.util.ArrayList;

/**
 *
 * @author elteir
 */
public class StatusChanger extends Thread {
    private String username, status;
    private Client client;

    public StatusChanger(String username, String status, Client client) {
        this.username = username;
        this.status = status;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
    
    public void run() {
        ArrayList<String> strs = new ArrayList();
        strs.add(username);
        strs.add(status);
        client.sendToServer(new Message(8, new StrNames(strs)));
    }
    
}
