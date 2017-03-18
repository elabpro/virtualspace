package interactive;

import interactive.speech.AbstractTextToSpeech;
import interactive.speech.TextToSpeechFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            DataInputStream in) throws IOException
    {
        System.out.println("Текст отправки: " + text);
        out.write(text.getBytes("UTF-8"));// отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
        byte[] b = new byte[1024];
        in.read(b);
        String answer = new String(b, "UTF-8");
        if (answer.length() > 1)
        {
            if (text.equals("--intellectual"))
            {
                System.out.println("Ответ от серверной части -i: " + answer);
                return;
            }
            System.out.println("Ответ от серверной части -d: " + answer);
            AbstractTextToSpeech tts
                    = TextToSpeechFactory.get(TextToSpeechFactory.IVONA_SOURCE);
            try
            {
                tts.textToVoice(answer);
            } catch (Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
    }

    private static void getOfferApplicationLaunch(DataOutputStream out,
            DataInputStream in) throws IOException
    {
        String text;
        String answer;
        do
        {
            text = "\0";
            out.write(text.getBytes("UTF-8"));// отсылаем введенную строку текста серверу.
            out.flush();
            byte[] b = new byte[1024];
            in.read(b);
            answer = new String(b, "UTF-8");
            System.out.println(answer + "\n");
        } while (!answer.equals("--end"));
    }
}
