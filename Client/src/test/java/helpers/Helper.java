package helpers;

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
     *
     * @return A Signer array, containing 1 signer.
     */
    public static Signer[] createDemoSigners() {
        return new Signer[]{Signer.builder().email("john.doe@example.com")
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
     * Create a main.java.demo transaction
     *
     * @param signers  The {@code Signer} array for the transaction
     * @return a main.java.demo {@code Transaction} object
     * @see nl.yurimeiburg.ondertekenen.objects.Signer
     * @see nl.yurimeiburg.ondertekenen.objects.Transaction
     */
    public static Transaction createDemoTransaction(Signer[] signers) {
        return Transaction.builder()
                .signers(signers)
                .seal(true)
                .reference("Contract #123")
                .sendEmailNotifications(true)
                .daysToExpire(90)
                .signRequestMode(1)
                .build();
    }

}
