package interactive.speech;


public abstract class AbstractTextToSpeech {

    public abstract void textToFile(String text, String file) throws Exception;

    public abstract void textToVoice(String text) throws Exception;

}
