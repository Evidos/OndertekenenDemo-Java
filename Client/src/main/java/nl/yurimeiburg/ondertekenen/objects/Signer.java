package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Data class containing Signer parameters, following:
 * https://api.signhost.com/Help/Api/POST-api-transaction
 */
@Builder
@Getter
public class Signer {
    private static final Logger LOGGER = LogManager.getLogger(Signer.class);

    @SerializedName("Id")
    private String id = null;
    @SerializedName("Email")
    private String email = null;
    @SerializedName("Mobile")
    private String mobile = null;
    @SerializedName("BSN")
    private String bsn;
    @SerializedName("RequireScribble")
    private boolean requireScribble = false;
    @SerializedName("RequireSmsVerification")
    private boolean requireSMSVerification = false;
    @SerializedName("RequireDigiDVerification")
    private boolean requireDigiDVerification = false;
    @SerializedName("RequireKennisnetVerification")
    private boolean requireKennisnetVerification = false;
    @SerializedName("RequireSurfnetVerification")
    private boolean requireSurfnetVerification = false;
    @SerializedName("Verifications")
    private Verification[] verifications;
    @SerializedName("SendSignRequest")
    private boolean sendSignRequest;
    @SerializedName("SignRequestMessage")
    private String signRequestMessage;
    @SerializedName("SendSignConfirmation")
    private boolean sendSignConfirmation;
    @SerializedName("Language")
    private String language = "nl-NL";
    @SerializedName("ScribbleName")
    private String scribbleName;
    @SerializedName("ScribbleNameFixed")
    private boolean scribbleNameFixed = false;
    @SerializedName("DaysToRemind")
    private int daysToRemind = 7;
    @SerializedName("Expires")
    private String expires;
    @SerializedName("Reference")
    private String reference;
    @SerializedName("ReturnUrl")
    private String returnUrl = "http://signhost.com";
    @SerializedName("Activities")
    private Activity[] activities;
}
