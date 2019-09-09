/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import importedpackage.Message;
import importedpackage.SocketData;
import importedpackage.StringContent;

/**
 *
 * @author elteir
 */
public class GroupCreator extends Thread {
    private Client client;
    private String groupName;

    public GroupCreator(Client client, String groupName) {
        this.client = client;
        this.groupName = groupName;
    }
    
    public void run() {
        client.sendToServer(new Message(3, new StringContent(groupName)));
    }
}
