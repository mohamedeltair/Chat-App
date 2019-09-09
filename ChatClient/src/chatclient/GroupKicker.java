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
public class GroupKicker extends Thread {
    private String groupName, username;
    private Client client;

    public GroupKicker(String groupName, String username, Client client) {
        this.groupName = groupName;
        this.username = username;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUsername() {
        return username;
    }
    public void run() {
        ArrayList<String> strs = new ArrayList();
        strs.add(groupName);
        strs.add(username);
        client.sendToServer(new Message(7, new StrNames(strs)));
    }
}
