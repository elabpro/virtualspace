/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
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
 * @author glebmillenium
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
    private static boolean change = false;
    DataInputStream in;
    DataOutputStream out;
    private static LiveSpeechRecognizer recognizer;

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

            recognizer = new LiveSpeechRecognizer(configuration);

            String utterance;
            recognizer.startRecognition(true);
            this.state = true;
            while (this.state)
            {
                if (change)
                {
                    System.out.println("Перезагружаем словарь");
                    recognizer.stopRecognition();
                    recognizer = new LiveSpeechRecognizer(configuration);
                    recognizer.startRecognition(true);
                    System.out.println("Словарь успешно перезагружен");
                    change = false;
                }
                System.out.println("Запущен процесс распознавания!");
                utterance = recognizer.getResult().getHypothesis();
                System.out.println("Процесс распознавания остановлен!");
                if (utterance.length() > 3)
                {
                    utterance = new String(utterance.getBytes(), "UTF-8");
                    System.out.println("Распознанная команда: " + utterance);
                    if (utterance.equals("ева включи жесты"))
                    {
                        TreatmenterVisualCommand.onHand();
                    }
                    if (utterance.equals("ева отключи жесты"))
                    {
                        TreatmenterVisualCommand.onDefault();
                    }
                    if (!utterance.equals("<unk>"))
                    {
                        String answer = ConnectWithRemoteManagerSocket.
                                sendMessage(utterance, in, out);
                        ExternalSupportModuleCommands.speech(answer);
                    }
                }
            }
            recognizer.stopRecognition();
        } catch (IOException ex)
        {
            Logger.getLogger(TreatmenterVoiceCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(TreatmenterVoiceCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateDictionary()
    {
        change = true;
    }

    /**
     * stopping остановка обработки голосовых команд
     *
     */
    public void stopping()
    {
        this.state = false;
    }
}
