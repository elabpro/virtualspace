package interactive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * подключение к удалённому сокету передавая голосовые, а также управление с
 * помощью жестов
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
    boolean checkCommands;
    private DataInputStream in;
    private DataOutputStream out;

    public ConnectWithRemoteManagerSocket() throws IOException
    {
        this.serverPort = 3425;
        this.address = "127.0.0.1";
        this.checkCommands = false;
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    public ConnectWithRemoteManagerSocket(String ip, int port) throws IOException
    {
        this.serverPort = port;
        this.address = ip;
        this.checkCommands = false;
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    public ConnectWithRemoteManagerSocket(String ip, int port, boolean check) throws IOException
    {
        this.serverPort = port;
        this.address = ip;
        this.checkCommands = check;
        socket = new Socket(InetAddress.getByName(address), serverPort);
    }

    /**
     * run Метод запускающий реализацию сокет соединения по протоколу TCP в
     * параллельном треде
     *
     * @param void
     * @return void
     */
    @Override
    public void run()
    {
        try
        {
            /**
             * Берем входной и выходной потоки сокета, теперь можем получать и
             * отсылать данные клиенту.
             *
             * @param InputStream - входной поток сокета.
             * @param OutputStream - выходной поток сокета.
             */
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            /**
             * Конвертация потоков в другой тип, чтоб легче обрабатывать
             * текстовые сообщения.
             *
             * @param DataInputStream входной поток данных
             * @param DataOutputStream выходной поток данных
             */
            in = new DataInputStream(sin);
            out = new DataOutputStream(sout);

            if (checkCommands)
            {
                checkerCommand();
            } else
            {
                senderCommand();
            }
        } catch (Exception x)
        {
            System.out.println(x.toString());
        }
    }

    private String CppToJavaString(String strcpp)
    {
        String result = "";
        for (int i = 0; i < strcpp.length(); i++)
        {
            if (strcpp.charAt(i) == '\0')
            {
                break;
            }
            result += strcpp.charAt(i);
        }
        return result;
    }

    private void checkerCommand() throws IOException, InterruptedException
    {
        String answer;
        int opcode = -1;
        int opcodeFromServer;
        ExternalSupportModuleCommands.speech("Идёт загрузка доступных команд с сервера");
        while (true)
        {
            answer = sendMessage("--opcode", in, out);
            opcodeFromServer = Integer.parseInt(CppToJavaString(answer));
            System.out.println("Состояние клиента: " + opcode
                    + " Состояние сервера: " + opcodeFromServer);
            if (opcode != opcodeFromServer)
            {
                answer = sendMessage("--get", in, out, 16384);
                if (answer.length() > 2)
                {
                    opcode = opcodeFromServer;
                    ExternalSupportModuleCommands.
                            updateDictionaryAndGraphicalText(answer);
                    System.out.println(answer);
                }
            }
            Thread.sleep(2500);
        }
    }

    private void senderCommand() throws IOException, InterruptedException
    {
        treatmenterVoiceCommand = new TreatmenterVoiceCommand(in, out);
        treatmenterVisualCommand = new TreatmenterVisualCommand();
        treatmenterVisualCommand.start();
        treatmenterVoiceCommand.start();
        String answer = sendMessage("--intellectual", in, out);
        ExternalSupportModuleCommands.intellectualManage(answer, in, out);
    }

    public static String sendMessage(String text, DataInputStream in,
            DataOutputStream out) throws IOException, InterruptedException
    {

        out.write((text + '\0').getBytes("UTF-8"));
        out.flush();
        byte[] b = new byte[1024];
        in.read(b);
        String answer = new String(b, "UTF-8");
        return answer;
    }

    public static String sendMessage(String text, DataInputStream in,
            DataOutputStream out, int sizeByte) throws IOException, InterruptedException
    {

        out.write((text + '\0').getBytes("UTF-8"));
        out.flush();
        byte[] b = new byte[sizeByte];
        in.read(b);
        String answer = new String(b, "UTF-8");
        return answer;
    }

    public void stopping()
    {
        if (!checkCommands)
        {
            treatmenterVoiceCommand.stopping();
            treatmenterVisualCommand.stopping();
        }
        this.stop();
    }
}
