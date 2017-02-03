package nl.yurimeiburg.ondertekenen.demo;

import nl.yurimeiburg.ondertekenen.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
                .requireScribble(true)
                .scribbleName("John Doe")
                .scribbleNameFixed(false)
                .reference("12344321")
                .returnUrl("https://example.com/thanks.php").build()
        };
    }

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

    public static File getPDFDemoFile() {
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

    public static FormSetField getDemoFormSetField() {
        return FormSetField
                .builder()
                .type(FormSetType.SINGLE_LINE)
                .location(
                        Location
                                .builder()
                                .top(20)
                                .right(20)
                                .width(300)
                                .height(50)
                                .pageNumber(1)
                                .build()
                )
                .build();
    }

    public static Map<String, FormSetField> getDemoFormSet1() {
        Map<String, FormSetField> formSet = new HashMap<>();
        formSet.put("SignatureOne", getDemoFormSetField());
        return formSet;
    }

    public static FormSetField getDemoFormSetField2() {
        return FormSetField
                .builder()
                .type(FormSetType.SIGNATURE)
                .location(
                        Location
                                .builder()
                                .top(20)
                                .right(20)
                                .width(300)
                                .height(50)
                                .pageNumber(2)
                                .build()
                )
                .build();
    }

    public static Map<String, FormSetField> getDemoFormSet2() {
        Map<String, FormSetField> formSet = new HashMap<>();
        formSet.put("Signature-2", getDemoFormSetField2());
        return formSet;
    }

    public static Map<String, Map<String, FormSetField>> getFormSets() {
        Map<String, Map<String, FormSetField>> formSets = new HashMap<>();
        formSets.put("FirstFormset", getDemoFormSet1());
        formSets.put("SecondSigner", getDemoFormSet2());
        return formSets;
    }
    public static FileMetaData getMetaDataDemo(String signerId) {
        return FileMetaData
                .builder()
                .displayName("Your personal contract")
                .formSets(getFormSets())
                .signers(Collections.singletonMap(
                        signerId,
                        FormSets
                                .builder()
                                .formSets(
                                        new String[]{"FirstFormset", "SecondSigner"})
                                .build()))
                .build();
    }


}
