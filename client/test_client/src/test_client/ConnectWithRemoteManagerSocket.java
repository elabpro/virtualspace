/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author glebmillenium
 */
public class ConnectWithRemoteManagerSocket extends Thread
{

    private int serverPort;
    private String address;
    private TreatmenterVoiceCommand treatmenterVoiceCommand;
    private TreatmenterVisualCommand treatmenterVisualCommand;
    private Socket socket;
    
    ConnectWithRemoteManagerSocket() throws IOException
    {
        this.serverPort = 3425;
        this.address = "127.0.0.1";
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    ConnectWithRemoteManagerSocket(String ip, int port) throws IOException
    {
        this.serverPort = port;
        this.address = ip;
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    public void run()
    {
        try
        {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            // Создаем поток для чтения с клавиатуры.
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            treatmenterVoiceCommand =  new TreatmenterVoiceCommand(in, out);
            treatmenterVisualCommand = new TreatmenterVisualCommand(in, out);
            treatmenterVoiceCommand.start();
            treatmenterVisualCommand.start();
        } catch (Exception x)
        {
            x.printStackTrace();
        }
    }
    
    public void sendMessage(){
        
    }
    
    public void stopping()
    {
        treatmenterVoiceCommand.stopping();
        treatmenterVisualCommand.stopping();
        this.stop();
    }
}
