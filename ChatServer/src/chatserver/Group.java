/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import importedpackage.Sender;
import importedpackage.User;
import java.util.ArrayList;

/**
 *
 * @author elteir
 */
public class Group {
    private String name;
    private ArrayList<Sender> senders;
    public Group(String name) {
        this.name = name;
        senders = new ArrayList();
    }
    public void addSender(Sender sender) {
       senders.add(sender);
    }
    public void removeSender(Sender sender) {
        for(Sender sender2: senders) {
            if(sender.equals(sender2)) {
                senders.remove(sender2);
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Sender> getSenders() {
        return senders;
    }
    
}
