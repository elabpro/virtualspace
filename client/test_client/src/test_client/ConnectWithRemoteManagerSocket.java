/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Thread;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author glebmillenium
 */
public class ConnectWithRemoteManagerSocket extends Thread
{

    int serverPort;
    String address;

    ConnectWithRemoteManagerSocket()
    {
        this.serverPort = 3425;
        this.address = "127.0.0.1";
    }

    ConnectWithRemoteManagerSocket(String ip, int port)
    {
        this.serverPort = port;
        this.address = ip;
    }

    public void run()
    {

        try
        {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Yes! I just got hold of the program.");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом. 
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            // Создаем поток для чтения с клавиатуры.
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            System.out.println();

            while (true)
            {
                //------
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                if (line.equals(":x"))
                {
                    break;
                }
                System.out.println("Sending this line to the server...");
                out.write(line.getBytes()); // отсылаем введенную строку текста серверу.
                out.flush(); // заставляем поток закончить передачу данных.
                System.out.println("Waiting answer from server");
                line += '\0';
                byte[] answer = new byte[line.length()];
                int count_get_byte = in.read(answer); // ждем пока сервер отошлет строку текста.
                String str = "";
                for (int i = 0; i < count_get_byte; i++)
                {
                    str += (char) answer[i];
                }
                System.out.println("The server was very polite. It sent me this : " + str);
                System.out.println("Looks like the server is pleased with us. Go ahead and enter more lines.");
                System.out.println();
                //-------
            }
        } catch (Exception x)
        {
            x.printStackTrace();
        }
    }

}
