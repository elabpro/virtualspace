/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import interactive.speech.AbstractTextToSpeech;
import interactive.speech.TextToSpeechFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TreatmenterVoiceCommand Класс для обработки аудиопотока
 *
 * Java version j8
 *
 * @license IrGUPS
 * @author glebmillenium
 * @link https://github.com/irgups/virtualspace
 */
public class TreatmenterVoiceCommand extends Thread
{

    private static final String ACOUSTIC_MODEL
            = "resources/ru-ru/";
    private static final String DICTIONARY_PATH
            = "resources/command.dict";
    private static final String GRAMMAR_PATH
            = "resources/";
    private boolean state = false;
    DataInputStream in;
    DataOutputStream out;

    /**
     * TreatmenterVoiceCommand Конструктор обеспечивающий передачу результатов
     * обработки аудиопотока через сокет
     *
     * @param in поток входных данных
     * @param out поток выходных данных
     */
    public TreatmenterVoiceCommand(DataInputStream in, DataOutputStream out)
    {
        this.in = in;
        this.out = out;
    }

    /**
     * run запуск обработки голосовых команд
     *
     * @param void
     * @return void
     */
    @Override
    public void run()
    {
        try
        {
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath(ACOUSTIC_MODEL);
            configuration.setDictionaryPath(DICTIONARY_PATH);
            configuration.setGrammarPath(GRAMMAR_PATH);
            configuration.setUseGrammar(true);
            configuration.setGrammarName("dialog");
            LiveSpeechRecognizer recognizer;
            recognizer = new LiveSpeechRecognizer(configuration);

            String utterance;
            recognizer.startRecognition(true);
            this.state = true;
            while (this.state)
            {
                utterance = recognizer.getResult().getHypothesis();
                System.out.println(utterance);
                if (utterance.length() > 7)
                {
                    sendMessageToServer(utterance);
                }
            }
            recognizer.stopRecognition();
        } catch (IOException ex)
        {
            Logger.getLogger(TreatmenterVoiceCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * stopping остановка обработки голосовых команд
     *
     * @param void
     * @return void
     */
    public void stopping()
    {
        this.state = false;
    }

    /**
     * sendMessageToServer отправка сообщения на сервер
     *
     * @param String text текстовое сообщение
     * @throws IOException
     * @return void
     */
    private void sendMessageToServer(String text) throws IOException
    {
        text += '\0';
        out.write((new String(text.getBytes(), "UTF-8")).getBytes()); // отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
        byte[] b = new byte[1024];
        in.read(b);
        String answer = new String(b, "UTF-8");
        System.out.println("Ответ от серверной части: " + answer);
        AbstractTextToSpeech tts
                = TextToSpeechFactory.get(TextToSpeechFactory.IVONA_SOURCE);
        try
        {
            tts.textToVoice(answer);
        } catch (Exception ex)
        {

        }
    }
}
