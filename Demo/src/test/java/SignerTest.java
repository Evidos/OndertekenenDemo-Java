import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.evidos.ondertekenen.gson.SignerActivityCodeDeserializer;
import nl.evidos.ondertekenen.objects.Signer;
import nl.evidos.ondertekenen.objects.SignerActivity;
import nl.evidos.ondertekenen.objects.SignerActivityCode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yuri Meiburg on 28-1-2015.
 */
public class SignerTest {
    private static final String VALID_MOBILE = "+31612345678";
    private static final String INVALID_MOBILE = "0612345678";

    private Gson gson;

    @Before
    public void init(){
        gson = new GsonBuilder()
                .registerTypeAdapter(SignerActivityCode.class, new SignerActivityCodeDeserializer())
                .create();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void mobileNumberFormatNoCountryCode(){
        new Signer("email").mobile(INVALID_MOBILE);
    }

    @Test
    public void mobileNumberFormatWithCountryCode(){
        Signer s = new Signer("email").mobile(VALID_MOBILE);
        assertEquals(VALID_MOBILE, s.getMobile());
    }


    /**
     * Take complete sample from https://api.signhost.com/Help/Api/GET-api-transaction-transactionId
     * and use that as reference.
     */
    @Test
    public void testIfJSonConversionWorks(){

        String jsonSample = " {\n" +
                "      \"Id\": \"3c5feb2e-f4ca-4701-96ce-8f3fc1e56cd5\",\n" +
                "      \"Email\": \"user@example.com\",\n" +
                "      \"Mobile\": \"+31612345678\",\n" +
                "      \"RequireScribble\": true,\n" +
                "      \"RequireEmailVerification\": true,\n" +
                "      \"RequireSmsVerification\": true,\n" +
                "      \"SendSignRequest\": true,\n" +
                "      \"SendSignConfirmation\": true,\n" +
                "      \"SignRequestMessage\": \"Hello, could you please sign this document? Best regards, John Doe\",\n" +
                "      \"DaysToRemind\": 15,\n" +
                "      \"Language\": \"en-US\",\n" +
                "      \"ScribbleName\": \"John Doe\",\n" +
                "      \"ScribbleNameFixed\": true,\n" +
                "      \"Reference\": \"Client #123\",\n" +
                "      \"ReturnUrl\": \"http://signhost.com\",\n" +
                "      \"Activities\": [\n" +
                "        {\n" +
                "          \"Id\": \"a4569cb4-d780-4586-b259-2650dd646b4c\",\n" +
                "          \"Code\": 103,\n" +
                "          \"Activity\": \"Opened\",\n" +
                "          \"CreatedDateTime\": \"2015-02-05T05:04:37.6227457+01:00\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": \"1b36093d-2c68-4c3a-b2ad-0ad7aca42937\",\n" +
                "          \"Code\": 203,\n" +
                "          \"Activity\": \"Signed\",\n" +
                "          \"CreatedDateTime\": \"2015-02-05T05:09:37.6227457+01:00\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"RejectReason\": null,\n" +
                "      \"SignUrl\": \"http://ui.signhost.com/sign/bd3ef567-0563-4f51-95dc-48a416c246d6\",\n" +
                "      \"SignedDateTime\": \"2015-03-05T05:04:37.6227457+01:00\",\n" +
                "      \"RejectDateTime\": \"2015-04-05T05:04:37.6227457+01:00\",\n" +
                "      \"CreatedDateTime\": \"2015-05-05T05:04:37.6227457+01:00\",\n" +
                "      \"ModifiedDateTime\": \"2015-06-05T05:04:37.6227457+01:00\"\n" +
                "    }";
        Signer signer = gson.fromJson(jsonSample, Signer.class);

        assertEquals("3c5feb2e-f4ca-4701-96ce-8f3fc1e56cd5", signer.getId());
        assertEquals("user@example.com", signer.getEMail());
        assertEquals("+31612345678", signer.getMobile());
        assertEquals(true, signer.isRequireScribble());
        assertEquals(true, signer.isRequireEmailVerification());
        assertEquals(true, signer.isRequireSMSVerification());
        assertEquals(true, signer.isSendSignRequest());
        assertEquals(true, signer.isSendSignConfirmation());
        assertEquals("Hello, could you please sign this document? Best regards, John Doe", signer.getSignRequestMessage());
        assertEquals(15, signer.getDaysToRemind());
        assertEquals("en-US", signer.getLanguage());
        assertEquals("John Doe", signer.getScribbleName());
        assertEquals("Client #123", signer.getReference());
        assertEquals("http://signhost.com", signer.getReturnUrl());
        assertEquals(null, signer.getRejectReason());
        assertEquals("http://ui.signhost.com/sign/bd3ef567-0563-4f51-95dc-48a416c246d6", signer.getSignUrl());
        assertEquals("2015-03-05T05:04:37.6227457+01:00", signer.getSignedDateTime());
        assertEquals("2015-04-05T05:04:37.6227457+01:00", signer.getRejectDateTime());
        assertEquals("2015-05-05T05:04:37.6227457+01:00", signer.getCreatedDateTime());
        assertEquals("2015-06-05T05:04:37.6227457+01:00", signer.getModifiedDateTime());


        // Also check activities
        SignerActivity [] signerActivities = signer.getActivities();
        assertEquals("a4569cb4-d780-4586-b259-2650dd646b4c",signerActivities[0].getId());
        assertEquals(SignerActivityCode.OPENED,signerActivities[0].getSignerActivityCode());
        assertEquals("Opened",signerActivities[0].getActivity());
        assertEquals("2015-02-05T05:04:37.6227457+01:00",signerActivities[0].getCreatedDateTime());

        assertEquals("1b36093d-2c68-4c3a-b2ad-0ad7aca42937",signerActivities[1].getId());
        assertEquals(SignerActivityCode.SIGNED,signerActivities[1].getSignerActivityCode());
        assertEquals("Signed",signerActivities[1].getActivity());
        assertEquals("2015-02-05T05:09:37.6227457+01:00",signerActivities[1].getCreatedDateTime());

    }

}
