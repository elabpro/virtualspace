package interactive;

import interactive.speech.AbstractTextToSpeech;
import interactive.speech.TextToSpeechFactory;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author glebmillenium
 */
public class ExchangeMessageWithServer
{

    /**
     * sendMessageToServer отправка сообщения на сервер
     *
     * @param text
     * @param out
     * @param in
     * @throws IOException
     * @return void
     */
    public static synchronized void sendMessage(String text,
            DataOutputStream out,
            DataInputStream in) throws IOException, InterruptedException
    {
        out.write(text.getBytes("UTF-8"));// отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
        byte[] b = new byte[1024];
        in.read(b);
        String answer = new String(b, "UTF-8");
        if (answer.length() > 1)
        {
            String[] answerArray = answer.split("\n");
            if (text.equals("--default\0"))
            {
                speech("Идёт загрузка доступных команд с сервера");
                System.out.println("Вот они" + answer);
                for (int i = 0; i < answerArray.length - 1; i++)
                {
                    String[] dictionaryConverterWIN = answerArray[i].split(" ");
                    String temp = "";
                    for (int j = 0; j < dictionaryConverterWIN.length; j++)
                    {
                        temp += " \"" + dictionaryConverterWIN[j] + "\"";
                    }
                    answerArray[i] = temp;
                }
                File file = new File("resources", "dialog.gram");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("resources/dialog.gram"), "UTF8"));
                //FileWriter writer = new FileWriter(file, false); // false - перезапись
                writer.write("#JSGF V1.0;\n" + "grammar dialog;\n" + "public <command> = ");
                for (int i = 1; i < answerArray.length - 1; i++)
                {
                    writer.write(answerArray[i] + " | ");
                }
                writer.write("\"\";");
                writer.flush();
                return;
            }
            if (text.equals("--intellectual\0"))
            {
                Thread.sleep(10000);
                speech("Интеллектуальное управление "
                        + "предлагает произвестие следующие действия");
                if (JOptionPane.showConfirmDialog(null,
                        "Система интеллектуального"
                        + " управления Ева предлагает "
                        + "вам выполнить следующие действия:\n" + answer,
                        "Интеллектуальное управление",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    for (String send : answerArray)
                    {
                        out.write(send.getBytes("UTF-8"));// отсылаем введенную строку текста серверу.
                        out.flush(); // заставляем поток закончить передачу данных.
                        in.read(b);
                        String[] intellectualAnswer
                                = (new String(b, "UTF-8")).split("\n");
                        speech(intellectualAnswer[0]);
                        Thread.sleep(2500);
                    }
                };
                return;
            }
            //answer from system
            speech(answerArray[0]);
            for (int i = 1; i < answerArray.length; i++)
            {
                String[] dictionaryConverterWIN = answerArray[i].split(" ");
                String temp = "";
                for (int j = 0; j < dictionaryConverterWIN.length; j++)
                {
                    temp += " \"" + dictionaryConverterWIN[j] + "\"";
                }
                answerArray[i] = temp;
            }
            File file = new File("resources", "dialog.gram");
            FileWriter writer = new FileWriter(file, false); // false - перезапись
            writer.write("#JSGF V1.0;\n" + "grammar dialog;\n" + "public <command> = ");
            for (int i = 1; i < answerArray.length - 1; i++)
            {
                writer.write(answerArray[i] + " | ");
            }
            writer.write("\"\";");
            writer.flush();
        }
    }

    private static void speech(String text)
    {
        AbstractTextToSpeech tts
                = TextToSpeechFactory.get(TextToSpeechFactory.IVONA_SOURCE);
        try
        {
            tts.textToVoice(text);
        } catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }

    private static void FileWriter(File file, boolean b)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
