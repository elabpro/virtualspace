package interactive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * подключение к удалённому сокету передавая голосовые, а также управление 
 * с помощью жестов
 * 
 * @param serverPort номер порта для подключения
 * @param address статический ip адрес для поключения сервера к клиенту
 * @param treatmenterVoiceCommand передача команд, с помощью управления голосом
 * @param treatmenterVisualCommand передача команд сделанных, с помощью жестов
 * @param socket создание ip адреса для локального узла
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

    /**
     * run
     * Метод запускающий реализацию 
     * сокет соединения по протоколу 
     * TCP в параллельном треде
     * 
     * @param  void
     * @return void
     */
    @Override
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

            treatmenterVoiceCommand =  new TreatmenterVoiceCommand(in, out);
            treatmenterVisualCommand = new TreatmenterVisualCommand(in, out);
            treatmenterVoiceCommand.start();
            treatmenterVisualCommand.start();
            ExchangeMessageWithServer.sendMessage("--intellectual", out, in);
        } catch (Exception x)
        {
        }
    }
    
    public void stopping()
    {
        treatmenterVoiceCommand.stopping();
        treatmenterVisualCommand.stopping();
        this.stop();
    }
}
