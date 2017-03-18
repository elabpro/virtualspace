package interactive;

import interactive.speech.AbstractTextToSpeech;
import interactive.speech.TextToSpeechFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        System.out.println("Текст отправки: " + text);
        out.write(text.getBytes("UTF-8"));// отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
        byte[] b = new byte[1024];
        in.read(b);
        String answer = new String(b, "UTF-8");
        if (answer.length() > 1)
        {
            String[] answerArray = answer.split("\n");
            if (text.equals("--intellectual"))
            {
                Thread.sleep(10000);
                speech("Интеллектуальное управление "
                        + "предлагает произвести "
                        + "следующие действия");
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
            //default answer from system
            speech(answerArray[0]);
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

}
