/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactive.speech;

/**
 *
 * @author anti1_000
 */
public class TextToSpeechFactory {

    public static final int IVONA_SOURCE = 1;

    public static AbstractTextToSpeech get(int type) throws UnsupportedOperationException {
        switch (type) {
            case 1:
                return new IvonaTextToSpeech();
            default:
                throw new UnsupportedOperationException("Wrong source type " + type);
        }
    }
}
