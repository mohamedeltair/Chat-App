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
public class GroupSender extends Thread {
    private String groupName, username, message;
    private Client client;

    public GroupSender(String groupName, String username, String message, Client client) {
        this.groupName = groupName;
        this.username = username;
        this.message = message;
        this.client = client;
    }
    
    

    public String getGroupName() {
        return groupName;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public Client getClient() {
        return client;
    }
    
    public void run() {
        ArrayList<String> strs = new ArrayList();
        strs.add(groupName);
        strs.add(username);
        strs.add(message);
        client.sendToServer(new Message(5, new StrNames(strs)));
    }
}
