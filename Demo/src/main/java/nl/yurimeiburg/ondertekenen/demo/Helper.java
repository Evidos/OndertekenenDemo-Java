package nl.yurimeiburg.ondertekenen.demo;

import nl.yurimeiburg.ondertekenen.objects.FileInfo;
import nl.yurimeiburg.ondertekenen.objects.Signer;
import nl.yurimeiburg.ondertekenen.objects.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Construct main.java.demo objects
 * This class is mainly intended to construct main.java.demo objects in such a way that the functionality can easily be demonstrated.
 * They are in no way intended for production use, merely to remove distracting objects from the demonstration.
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class Helper {
    private static final Logger LOGGER = LogManager.getLogger(Helper.class);

    /**
     * Create a Signer array, containing signers with main.java.demo data
     *
     * @return A Signer array, containing 1 signer.
     */
    public static Signer[] createDemoSigners() {

        return new Signer[]{Signer.builder().email("y.meiburg+signhost@gmail.com")
                .mobile("+31612345678")
                .requireScribble(false)
                .requireSMSVerification(false)
                .sendSignRequest(true)
                .signRequestMessage("Will you please sign this document for me?")
                .sendSignConfirmation(true)
                .language("nl-NL")
                .scribbleName("John Doe")
                .scribbleNameFixed(false)
                .reference("12344321")
                .returnUrl("https://example.com/thanks.php").build()
        };
    }

    /**
     * Create a new FileInfo object
     *
     * @return return a main.java.demo {@code FileInfo} object.
     * @see nl.yurimeiburg.ondertekenen.objects.FileInfo
     */
    public static FileInfo createDemoFileInfo() {
        return new FileInfo("Input.pdf");
    }

    public static Transaction createDemoTransaction(FileInfo fileInfo, Signer[] signers) {
        return Transaction.builder()
                .signers(signers)
                .seal(true)
                .reference("Contract #123")
                .sendEmailNotifications(true)
                .daysToExpire(90)
                .signRequestMode(1)
                .build();
    }

    public static File getDemoFile() {
        try {
            URL location = Helper.class.getClassLoader().getResource("Input.pdf");
            if (location == null) {
                LOGGER.error("File does not exist, returning empty File");
                return new File("");
            }
            return new File(location.toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("Could not find Demo file");
            return new File("");
        }
    }


}
