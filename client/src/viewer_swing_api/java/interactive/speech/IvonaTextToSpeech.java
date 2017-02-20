package interactive.speech;

import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.CreateSpeechResult;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.Voice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import javazoom.jl.player.Player;

/**
 *
 * @author anti1_000
 */
public class IvonaTextToSpeech extends AbstractTextToSpeech {

    private static IvonaSpeechCloudClient speechCloud;

    private static void init() {
        Properties prop = new Properties();
        prop.setProperty("accessKey", "GDNAJHT7N4RGDADUSCRA");
        prop.setProperty("secretKey", "xTYZpPi/jH9VRKaq9YvZ0KsmHrxMD3BWJd1/v/0D");
        speechCloud = new IvonaSpeechCloudClient(new PropertiesFileCredentialsProvider("resources/speech/IvonaCredentials.properties"));
        speechCloud.setEndpoint("https://tts.eu-west-1.ivonacloud.com");
    }

    @Override
    public void textToFile(String text, String file) throws Exception {
        init();

        String outputFileName = file;
        CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
        Input input = new Input();
        Voice voice = new Voice();

        voice.setName("Tatyana");//указывается голос м - Maxim
        input.setData(text);

        createSpeechRequest.setInput(input);
        createSpeechRequest.setVoice(voice);
        CreateSpeechResult createSpeechResult = speechCloud.createSpeech(createSpeechRequest);
        try (InputStream in = createSpeechResult.getBody();
                FileOutputStream outputStream = new FileOutputStream(new File(outputFileName));) {
            System.out.println("\nSuccess sending request:");
            System.out.println(" content type:\t" + createSpeechResult.getContentType());
            System.out.println(" request id:\t" + createSpeechResult.getTtsRequestId());
            System.out.println(" request chars:\t" + createSpeechResult.getTtsRequestCharacters());
            System.out.println(" request units:\t" + createSpeechResult.getTtsRequestUnits());

            System.out.println("\nStarting to retrieve audio stream:");

            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            while ((readBytes = in.read(buffer)) > 0) {

                System.out.println(" received bytes: " + readBytes);
                outputStream.write(buffer, 0, readBytes);
            }

            System.out.println("\nFile saved: " + outputFileName);
            FileInputStream fis = new FileInputStream(outputFileName);
        }
    }

    @Override
    public void textToVoice(String text) throws Exception {
        String outputFileName = "temp.mp3";
        textToFile(text, outputFileName);
        try (FileInputStream fis = new FileInputStream(outputFileName);) {
            Player playMP3 = new Player(fis);
            playMP3.play();
        }
    }

}
