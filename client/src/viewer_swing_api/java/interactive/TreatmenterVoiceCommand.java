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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
    DataInputStream in;
    DataOutputStream out;

    public TreatmenterVoiceCommand(DataInputStream in, DataOutputStream out){
        this.in = in;
        this.out = out;
    }
    
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
                if(utterance.equals("покер")) sendMessageToServer("poker");
            }
            recognizer.stopRecognition();
        } catch (IOException ex)
        {
            Logger.getLogger(TreatmenterVoiceCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopping()
    {
        this.state = false;
    }
    
    private void sendMessageToServer(String text) throws IOException{
        text += '\0';
        out.write(text.getBytes()); // отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
    }
}
