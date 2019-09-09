/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import importedpackage.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author elteir
 */
public class Reader extends Thread {
    private Socket s;
    private ObjectInputStream ois;
    private Client client;
    private String otherUsername;
    public Reader(Socket s, Client client) {
        this.s = s;
        this.client = client;
    }
    public void run() {
        try {
            ois = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true) {
            try {
                Message m = (Message)ois.readObject();
                decide(m);
            } catch (Exception ex) {
                if(s.getInetAddress().getHostAddress().equals(client.getServerIP()) &&
                        s.getPort() == client.getServerPort()) {
                    JOptionPane.showMessageDialog(client.getMainWindow(), "server is not connected, app will close");
                    System.exit(0);
                }
                else {
                    for(Sender sender: client.getSenders()) {
                        if(sender.getUsername().equals(otherUsername)) {
                            client.removeSender(sender);
                            break;
                        }
                    }
                    for(P2PWindow p2pwin: client.getP2pwindows()) {
                        if(p2pwin.getUsername().equals(otherUsername)) {
                            if(p2pwin.isVisible()) {
                                JOptionPane.showMessageDialog(p2pwin, otherUsername + " is offline now, "
                                        + "the chat will close");
                            }
                            p2pwin.setVisible(false);
                            client.removeP2PWindow(p2pwin);
                            break;
                        }
                    }
                }
                return;
            }
        }
    }
    public void decide(Message m) {
        try {
            switch(m.getID()) {
                case 0: {
                    System.out.println("got 0");
                    SocketDataExchange sde = (SocketDataExchange)m.getContent();
                    Socket newSocket = new Socket(sde.getSd2().getIP(), sde.getSd2().getPort());
                    System.out.println("connected with other user");
                    String username = client.getUsername(sde.getSd1());
                    client.addSender(newSocket, username);
                    client.findSender(Utilities.getSocketData(newSocket), 
                            new Message(10, new StringContent(client.getUsername())));
                    P2PWindow window = new P2PWindow(Utilities.getSocketData(newSocket), username,
                            client.getUsername(), client.getListener().getListenerData(), client,
                    Utilities.readFile("u_"+username+"_"+client.getUsername(), StandardCharsets.UTF_8));
                    client.addP2PWindow(window);
                    client.sendToServer(new Message(1, new SocketDataExchange(sde.getSd1(), 
                    client.getListener().getListenerData())));
                }
                break;
                case 1: {
                    System.out.println("got 1");
                    SocketDataExchange sd = (SocketDataExchange)m.getContent();
                    Socket newSocket = new Socket(sd.getSd2().getIP(), sd.getSd2().getPort());
                    System.out.println("connected with other user");
                    String username = client.getUsername(sd.getSd1()); 
                    client.addSender(newSocket, username);
                    client.findSender(Utilities.getSocketData(newSocket), 
                            new Message(10, new StringContent(client.getUsername())));
                    P2PWindow window = new P2PWindow(Utilities.getSocketData(newSocket), username,
                            client.getUsername(), client.getListener().getListenerData(),client,
                    Utilities.readFile("u_"+username+"_"+client.getUsername(), StandardCharsets.UTF_8));
                    window.setVisible(true);
                    client.addP2PWindow(window);
                }
                break;
                case 2: {
                    System.out.println("got 2");
                    OnlineUsers ou = (OnlineUsers)m.getContent();
                    client.setUsers(ou.getUsers());
                    MainWindow mainWindow = client.getMainWindow();
                    mainWindow.clearTable();
                    for(User user: ou.getUsers()) {
                        mainWindow.addRow(user.getUsername(), user.getStatus());
                    }
                }
                break;
                case 3: {
                    System.out.println("got 3");
                    P2PMessage message = (P2PMessage)m.getContent();
                    client.gotP2P(message);
                }
                break;
                case 4: {
                    System.out.println("got 4");
                    client.getMainWindow().clearTable2();
                    ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                    for(String name: names) {
                        client.getMainWindow().addRow2(name);
                    }
                }
                break;
                case 5: {
                    System.out.println("got 5");
                    ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                    String gName = names.get(0);
                    GroupWindow gWin = client.getGroupWindow(gName);
                    gWin.clearTable();
                    for(int i=1; i<names.size(); i++) {
                        gWin.addRow(names.get(i));
                    }
                }
                break;
                case 6: {
                    System.out.println("got 6");
                    MemberEdit me = (MemberEdit)m.getContent();
                    if(me.isAdd()) {
                        client.addGroupWindow(me.getGroupName(), me.getStart());
                    }
                    else {
                        client.removeGroupWindow(me.getGroupName());
                    }
                    
                }
                break;
                case 7: {
                    System.out.println("got 7");
                    ArrayList<String> names = ((StrNames)m.getContent()).getNames();
                    GroupWindow gw = client.getGroupWindow(names.get(0));
                    gw.addText(names.get(1) + ": " + names.get(2));
                    gw.setVisible(true);
                }
                break;
                case 8: {
                    System.out.println("got 8");
                    String message = ((StringContent)m.getContent()).getStr();
                    JOptionPane.showMessageDialog(client.getMainWindow(), message);
                    System.exit(0);
                }
                break;
                case 9: {
                    System.out.println("got 9");
                    String message = ((StringContent)m.getContent()).getStr();
                    JOptionPane.showMessageDialog(client.getMainWindow(), message);
                }
                break;
                case 10: {
                    this.otherUsername = new String(((StringContent)m.getContent()).getStr());
                }
            }
        }
        catch(Exception e) {
            
        }
    }
}
