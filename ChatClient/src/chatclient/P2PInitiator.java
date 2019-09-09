/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author elteir
 */
public class P2PInitiator extends Thread {
    private Client client;
    private int selectedRow;

    public P2PInitiator(Client client, int selectedRow) {
        this.client = client;
        this.selectedRow = selectedRow;
    }
    
    public void run() {
        client.requestP2P(selectedRow);
    }
}
