package helpers;

import nl.yurimeiburg.ondertekenen.objects.FileInfo;
import nl.yurimeiburg.ondertekenen.objects.Signer;
import nl.yurimeiburg.ondertekenen.objects.Transaction;

/**
 * Construct main.java.demo objects
 * This class is mainly intended to construct main.java.demo objects in such a way that the functionality can easily be demonstrated.
 * They are in no way intended for production use, merely to remove distracting objects from the demonstration.
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class Helper {

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

}
