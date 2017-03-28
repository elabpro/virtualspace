/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import interactive.speech.AbstractTextToSpeech;
import interactive.speech.TextToSpeechFactory;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author glebmillenium
 */
public class ExternalSupportModuleCommands
{

    private static AbstractTextToSpeech tts
            = TextToSpeechFactory.get(TextToSpeechFactory.IVONA_SOURCE);

    public static void intellectualManage(String answer, DataInputStream in,
            DataOutputStream out) throws InterruptedException, IOException
    {
        String[] answerArray = answer.split("\n");
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
            byte[] b = new byte[1024];
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
    }

    public static synchronized void speech(String text)
    {
        try
        {
            tts.textToVoice(text);
        } catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }

    private static boolean compareString(String answerFromServer, String condition)
    {
        boolean not_unknown = true;
        for (int i = 0; i < condition.length(); i++)
        {
            if (answerFromServer.charAt(i) != condition.charAt(i))
            {
                not_unknown = false;
            }
        }
        return not_unknown;
    }

    public static void updateGraphicalText(String answer) throws IOException
    {
        MainFrame.updateText(answer);
    }

    public static void updateDictionary(String answer) throws IOException
    {
        String[] answerArray = answer.split("\n");
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
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("resources/dialog.gram"), "UTF8"));
        writer.write("#JSGF V1.0;\n" + "grammar dialog;\n" + "public <command> = ");
        for (int i = 0; i < answerArray.length - 1; i++)
        {
            writer.write(answerArray[i] + " | ");
        }
        writer.write("\"\";");
        writer.flush();
        TreatmenterVoiceCommand.updateDictionary();
    }
}
