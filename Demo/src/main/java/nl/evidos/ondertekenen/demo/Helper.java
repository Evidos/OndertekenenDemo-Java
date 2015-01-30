package nl.evidos.ondertekenen.demo;

import nl.evidos.ondertekenen.objects.FileInfo;
import nl.evidos.ondertekenen.objects.Signer;
import nl.evidos.ondertekenen.objects.Transaction;

/**
 * Construct demo objects
 * This class is mainly intended to construct demo objects in such a way that the functionality can easily be demonstrated.
 * They are in no way intended for production use, merely to remove distracting objects from the demonstration.
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class Helper {

    /**
     * Create a Signer array, containing signers with demo data
     * @return A Signer array, containing 1 signer.
     */
    public static Signer[] createDemoSigners(){
        return new Signer [] { new Signer("yuri@meiburg.nl")
                .mobile("+31630789696")
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
     * @return return a demo {@code FileInfo} object.
     * @see nl.evidos.ondertekenen.objects.FileInfo
     */
    public static FileInfo createDemoFileInfo(){
        return new FileInfo("test.pdf");
    }

    /**
     * Createa demo transaction
     * @param fileInfo The {@code FileInfo} for the transaction
     * @param signers The {@code Signer} array for the transaction
     * @see nl.evidos.ondertekenen.objects.FileInfo
     * @see nl.evidos.ondertekenen.objects.Signer
     * @see nl.evidos.ondertekenen.objects.Transaction
     * @return a demo {@code Transaction} object
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
