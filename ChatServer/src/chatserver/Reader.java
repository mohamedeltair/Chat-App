/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.*;
import java.net.*;
import importedpackage.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author elteir
 */
public class Reader extends Thread {
    Socket socket;
    ObjectInputStream ois;
    Server server;

    public Reader(Socket socket, Server server) {
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            this.server = server;
        } catch (IOException ex) {

        }
    }
    public void run() {
        while(true) {
            try {
                Message m = (Message)ois.readObject();
                decide(m);
                
            } catch (Exception ex) {
                System.out.println("lost connection with user");
                String name = server.socketToName(socket);
                server.removeFromAllGroups(name);
                server.removeSender(socket);
                server.postOnline();
                return;
            }
        }
    }
    
    public void decide(Message m) {
        switch(m.getID()) {
            case 0: {
                System.out.println("got 0");
                SocketDataExchange sde = (SocketDataExchange)m.getContent();
                server.findSender(sde.getSd1(), new Message(0, new SocketDataExchange(Utilities.getSocketData(socket), 
                sde.getSd2())));
            }
            break;
            case 1: {
                System.out.println("got 1");
                SocketDataExchange sde = (SocketDataExchange)m.getContent();
                server.findSender(sde.getSd1(), new Message(1, new SocketDataExchange(Utilities.getSocketData(socket),
                        sde.getSd2())));
            }
            break;
            case 2: {
                System.out.println("got 2");
                StringContent strC = (StringContent)m.getContent();
                System.out.println("Hello "+strC.getStr());
                server.addSender(socket, strC.getStr());
                server.postOnline();
                server.postGroups();
            }
            break;
            case 3: {
                System.out.println("got 3");
                StringContent strC = (StringContent)m.getContent();
                Group group = new Group(strC.getStr());
                server.addGroup(group);
                server.postGroups();
            }
            break;
            case 4: {
                System.out.println("got 4");
                MemberEdit me = (MemberEdit)m.getContent();
                Sender mysender = server.usernameToSender(me.getUsername());
                if(me.isAdd()) {
                    server.addGroupMember(me.getGroupName(), mysender);
                }
                else {
                    server.removeGroupMember(me.getGroupName(), mysender);
                }
                mysender.send(new Message(6, new MemberEdit(me.getUsername(), me.getGroupName(), me.isAdd(),
                Utilities.readFile("g_"+me.getGroupName(), StandardCharsets.UTF_8))));
                String name = me.getGroupName();
                server.postGroupMembers(name);
            }
            break;
            case 5: {
                System.out.println("got 5");
                ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                Group g = server.nameToGroup(names.get(0));
                for(Sender sender: g.getSenders()) {
                    sender.send(new Message(7, new StrNames(names)));
                }
                Utilities.writeToFile("g_"+g.getName(), names.get(1) + ": " + names.get(2));
            }
            break;
            case 6: {
                System.out.println("got 6");
                String name = ((StringContent)m.getContent()).getStr();
                server.removeFromAllGroups(name);
                Sender mySender = server.usernameToSender(name);
                mySender.send(new Message(8, new StringContent("You are kicked from the app")));
            }
            break;
            case 7: {
                System.out.println("got 7");
                ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                String groupname = names.get(0), username = names.get(1);
                Sender mysender = server.usernameToSender(username);
                server.removeGroupMember(groupname, mysender);
                mysender.send(new Message(6, new MemberEdit(username, groupname, false, "")));
                server.postGroupMembers(groupname);
                mysender.send(new Message(9, new StringContent("You are kicked from group "+groupname)));
            }
            break;
            case 8: {
                System.out.println("got 8");
                ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                String username = names.get(0), status = names.get(1);
                Sender sender = server.usernameToSender(username);
                sender.setStatus(status);
                server.postOnline();
            }
        }
    }
}
