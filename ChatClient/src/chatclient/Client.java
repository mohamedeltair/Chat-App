/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import importedpackage.*;
import javax.swing.JOptionPane;

/**
 *
 * @author elteir
 */

public class Client extends Thread {
    private Socket s;
    private String serverIP;
    private int serverPort;
    private ArrayList<Sender> senders;
    private ArrayList<User> users;
    private Listener listener;
    private String username;
    private MainWindow mainWindow;
    private ArrayList<P2PWindow> p2pwindows;
    private ArrayList<GroupWindow> groupwindows;
    
    public Client(String username, MainWindow mainWindow) {
        this.username = username;
        this.mainWindow = mainWindow;
        serverIP = "172.16.106.182";
        serverPort = 8080;
        users = new ArrayList();
        senders = new ArrayList();
        p2pwindows = new ArrayList();
        groupwindows = new ArrayList();
    }
    public void run() {
        try {
            s = new Socket(serverIP, serverPort);
            Reader r = new Reader(s, this);
            r.start();
            addSender(s, "server");
            System.out.println("I am "+username);
            sendToServer(new Message(2, new StringContent(username)));
            listener = new Listener(this);
            listener.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainWindow, "server is not connected, app will close");
            System.exit(0);
        }
        
    }
    public void requestP2P(int selectedRow) {
        try {
            sendToServer(new Message(0,
                    new SocketDataExchange(users.get(selectedRow).getSocketData(), listener.getListenerData())));
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void findSender(SocketData sd, Message m) {
        for(int i=senders.size()-1; i>=0; i--) {
            Sender sender = senders.get(i);
            if(sd.equals(Utilities.getSocketData(sender.getSocket()))) {
                sender.send(m);
                break;
            }
        }
    }
    
    public void addSender(Socket s, String username) {
        senders.add(new Sender(s, username));
    }
    
    public void addP2PWindow(P2PWindow window) {
        p2pwindows.add(window);
    }

    public Listener getListener() {
        return listener;
    }
    
    public boolean existsInP2P(SocketData socketData) {
        for(P2PWindow window : p2pwindows) {
            if(socketData.equals(window.getSocketData())) {
                return true;
            }
        }
        return false;
    }
    
    
    public String getUsername(SocketData s) {
        for(User user: users) {
            if(s.equals(user.getSocketData())) {
                return user.getUsername();
            }
        }
        return "-1";
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getUsername() {
        return username;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
    
    public void gotP2P(P2PMessage message) {
        for(P2PWindow window: p2pwindows) {
            if(window.getSocketData().equals(message.getSocketData())) {
                window.setVisible(true);
                window.addText(window.getUsername()+": "+message.getMessage());
                Utilities.writeToFile("u_"+window.getUsername()+"_"+this.username, 
                        window.getUsername()+": "+message.getMessage());
            }
        }
    }
    
    public void sendToServer(Message m) {
        senders.get(0).send(m);
    }
    
    public GroupWindow getGroupWindow(String name) {
        for(GroupWindow gWin: groupwindows) {
            if(gWin.getGroupName().equals(name)) {
                return gWin;
            }
        }
        System.out.println("didn't find group with name "+name);
        return null;
    }
    
    public void addGroupWindow(String name, String start) {
        groupwindows.add(new GroupWindow(name, username, this, start));
        System.out.println("now i have group instance " + name);
    }
    
    public void removeGroupWindow(String name) {
        for(GroupWindow gWin: groupwindows) {
            if(gWin.getGroupName().equals(name)) {
                gWin.setVisible(false);
                groupwindows.remove(gWin);
                break;
            }
        }
    }

    public ArrayList<GroupWindow> getGroupwindows() {
        return groupwindows;
    }

    public ArrayList<P2PWindow> getP2pwindows() {
        return p2pwindows;
    }

    public ArrayList<Sender> getSenders() {
        return senders;
    }
    
    public void removeSender(Sender sender) {
        senders.remove(sender);
    }
    
    public void removeP2PWindow(P2PWindow p2pwindow) {
        p2pwindows.remove(p2pwindow);
    }
    
}
