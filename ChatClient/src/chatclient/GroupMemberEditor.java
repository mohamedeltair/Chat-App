/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import importedpackage.MemberEdit;
import importedpackage.Message;

/**
 *
 * @author elteir
 */
public class GroupMemberEditor extends Thread {
    private Client client;
    private String groupName;
    private boolean add;

    public GroupMemberEditor(Client client, String groupName, boolean add) {
        this.client = client;
        this.groupName = groupName;
        this.add = add;
    }


    public Client getClient() {
        return client;
    }

    public boolean isAdd() {
        return add;
    }

    public String getGroupName() {
        return groupName;
    }
    
    public void run() {
        client.sendToServer(new Message(4, new MemberEdit(client.getUsername(), groupName, add, "")));
    }
}
