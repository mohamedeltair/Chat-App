/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import importedpackage.*;

/**
 *
 * @author elteir
 */
public class Server {
    private int port;
    private ServerSocket ss;
    private ArrayList<Sender> senders;
    private ArrayList<Group> groups;
    
    public Server() {
        port = 8080;
        senders = new ArrayList();
        groups = new ArrayList();    
        try {
            ss = new ServerSocket(port);
        } catch (IOException ex) {
        }
    }
    
    public void listen() {
        while(true) {
            try {
                Socket s = ss.accept();
                System.out.println("rec a connection");
                Reader r = new Reader(s, this);
                r.start();
            } catch (Exception ex) {
                return;
            }
        }
    }
    public void removeSender(Socket s) {
        for(Sender sender: senders) {
            if(Utilities.SocketEquals(s, sender.getSocket())) {
                senders.remove(sender);
                break;
            }
        }
    }
    public void addSender(Socket s, String username) {
        senders.add(new Sender(s, username));
    }
    
    public void findSender(SocketData sd, Message m) {
        for(Sender sender: senders) {
            if(sd.equals(Utilities.getSocketData(sender.getSocket()))) {
                sender.send(m);
                break;
            }
        }
    }
    
    public void postOnline() {
        ArrayList<User> users = new ArrayList();
        for(Sender sender: senders) {
            users.add(new User(sender.getUsername(), new SocketData(sender.getSocket()), sender.getStatus()));
        }
        for(Sender sender: senders) {
            sender.send(new Message(2, new OnlineUsers(users)));
        }
    }
    
    public void addGroup(Group group) {
        groups.add(group);
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Sender> getSenders() {
        return senders;
    }
    
    public void postGroups() {
        ArrayList<String> names = new ArrayList();
        for(Group g: groups) {
            names.add(g.getName());
        }
        for(Sender sender: senders) {
            sender.send(new Message(4, new StrNames(names)));
        }
    }
    
    public Sender usernameToSender(String username){
        for(Sender sender: senders) {
            if(username.equals(sender.getUsername())) {
                return sender;
            }
        }
        return null;
    }
    
    public void addGroupMember(String groupName, Sender sender) {
        for(Group group: groups) {
            if(group.getName().equals(groupName)) {
                group.addSender(sender);
            }
        }
    }
    
    public void removeGroupMember(String groupName, Sender sender) {
        for(Group group: groups) {
            if(group.getName().equals(groupName)) {
                for(Sender sender2: group.getSenders()) {
                    if(sender2.equals(sender)) {
                        group.removeSender(sender2);
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public Group nameToGroup(String name) {
        for(Group group: groups) {
            if(group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }
    
    public void postGroupMembers(String name) {
        Group group = nameToGroup(name);
        ArrayList<String> names = new ArrayList();
        names.add(group.getName());
        for(Sender sender: group.getSenders()) {
            names.add(sender.getUsername());
        }
        for(Sender sender: group.getSenders()) {
            sender.send(new Message(5, new StrNames(names)));
        }   
    }
    
    public boolean existsInGroup(Group group, String username) {
        for(Sender sender: group.getSenders()) {
            if(sender.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    
    public void removeFromAllGroups(String name) {
        Sender mySender = usernameToSender(name);
        for(Group group: groups) {
            if(existsInGroup(group, name)) {
                removeGroupMember(group.getName(), mySender);
                mySender.send(new Message(6, new MemberEdit(name, group.getName(), false, "")));
                postGroupMembers(group.getName());
            }
        }
    }
    
    public String socketToName(Socket socket) {
        for(Sender sender: senders) {
            if(Utilities.SocketEquals(socket, sender.getSocket())) {
                return sender.getUsername();
            }
        }
        return null;
    }
}
