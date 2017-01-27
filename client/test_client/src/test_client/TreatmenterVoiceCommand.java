/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_client;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
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
}
