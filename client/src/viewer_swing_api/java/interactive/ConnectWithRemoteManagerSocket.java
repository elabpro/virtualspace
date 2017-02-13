/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

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

/**
 * подключение к удалённому сокету передавая голосовые, а также управление 
 * с помощью жестов
 * 
 * @param serverPort номер порта для подключения
 * @param address статический ip адрес для поключения сервера к клиенту
 * @param treatmenterVoiceCommand передача команд, с помощью управления голосом
 * @param treatmenterVisualCommand передача команд сделанных, с помощью жестов
 * @param socket создание ip адреса для локального узла
 */
public class ConnectWithRemoteManagerSocket extends Thread
{

    private int serverPort;
    private String address;
    private TreatmenterVoiceCommand treatmenterVoiceCommand;
    private TreatmenterVisualCommand treatmenterVisualCommand;
    private Socket socket;
    
    public ConnectWithRemoteManagerSocket() throws IOException
    {
        this.serverPort = 3425;
        this.address = "127.0.0.1";
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    public ConnectWithRemoteManagerSocket(String ip, int port) throws IOException
    {
        this.serverPort = port;
        this.address = ip;
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    public void run()
    {
        try
        {
    /**
     * Берем входной и выходной потоки сокета, 
     * теперь можем получать и отсылать данные клиенту. 
     * @param InputStream - входной поток сокета.
     * @param InputStream - выходной поток сокета.
     */
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

/**
 * Конвертация потоков в другой тип, чтоб легче обрабатывать текстовые сообщения.
 * @param DataInputStream входной поток данных
 * @param DataOutputStream выходной поток данных
 */
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            // Создаем поток для чтения с клавиатуры.
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            treatmenterVoiceCommand =  new TreatmenterVoiceCommand(in, out);
            treatmenterVisualCommand = new TreatmenterVisualCommand(in, out);
            treatmenterVoiceCommand.start();
            treatmenterVisualCommand.start();
            
            /*while (this.isAlive())
            {
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                if (line.equals(":x"))
                {
                    treatmenterVoiceCommand.stopping();
                    treatmenterVisualCommand.stopping();
                    stopping();
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
            }*/
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
