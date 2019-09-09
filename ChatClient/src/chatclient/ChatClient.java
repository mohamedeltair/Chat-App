/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.util.*;
import importedpackage.*;
/**
 *
 * @author elteir
 */

public class ChatClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Client c = new Client();
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();
            if(choice.equals("yes")) {
            String ip = sc.next();
            String portst = sc.next();
            int port = Integer.parseInt(portst);
            c.requestP2P(new SocketData(ip, port));
        }*/
        Login login = new Login();
        login.setVisible(true);
    }
    
}
