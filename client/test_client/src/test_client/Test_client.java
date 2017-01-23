/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_client;
import java.net.*;
import java.io.*;

/**
 *
 * @author glebmillenium
 */
public class Test_client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ConnectWithRemoteManagerSocket socket = new ConnectWithRemoteManagerSocket();
        socket.start();
    }
    
}
