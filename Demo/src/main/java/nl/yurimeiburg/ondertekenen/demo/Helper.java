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
     * @return A Signer array, containing 1 signer.
     */
    public static Signer[] createDemoSigners(){
        return new Signer [] { new Signer("yuri@meiburg.nl")
                .mobile("+31612345678")
                .requireScribble(false)
                .requireEmailVerification(true)
                .requireSMSVerification(false)
                .sendSignRequest(true, "Will you please sign this document for me?")
                .sendSignConfirmation(true)
                .language("nl-NL")
                .scribbleName("Yuri Meiburg", false)
                .reference("12344321")
                .returnUrl("http://www.yurimeiburg.nl/") };
    }

    /**
     * Create a new FileInfo object
     * @return return a main.java.demo {@code FileInfo} object.
     * @see nl.yurimeiburg.ondertekenen.objects.FileInfo
     */
    public static FileInfo createDemoFileInfo(){
        return new FileInfo("Input.pdf");
    }

    /**
     * Create a main.java.demo transaction
     * @param fileInfo The {@code FileInfo} for the transaction
     * @param signers The {@code Signer} array for the transaction
     * @see nl.yurimeiburg.ondertekenen.objects.FileInfo
     * @see nl.yurimeiburg.ondertekenen.objects.Signer
     * @see nl.yurimeiburg.ondertekenen.objects.Transaction
     * @return a main.java.demo {@code Transaction} object
     */
    public static Transaction createDemoTransaction(FileInfo fileInfo, Signer [] signers){
        return new Transaction(fileInfo, signers)
                .seal(true)
                .reference("Contract #123")
                .sendEmailNotifications(true)
                .daysToExpire(90)
                .signRequestMode(1)
                .daysToReminder(7);
    }

    public static File getDemoFile(){
        try {
            URL location = Helper.class.getClassLoader().getResource("Input.pdf");
            if(location == null){
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
