package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Data class containing Signer parameters, following:
 * https://api.signhost.com/Help/Api/POST-api-transaction
 */
public class Signer {
    private static final Logger LOGGER = LogManager.getLogger(Signer.class);

    @SerializedName("EMail")
    private String eMail = null;
    @SerializedName("mobile")
    private String mobile = null;
    @SerializedName("requireScribble")
    private boolean requireScribble;
    @SerializedName("RequireEmailVerification")
    private boolean requireEmailVerification; // NOT IMPLEMENTED YET
    @SerializedName("RequireSMSVerification")
    private boolean requireSMSVerification;
    @SerializedName("SendSignRequest")
    private boolean sendSignRequest;
    @SerializedName("SignRequestMessage")
    private String signRequestMessage;
    @SerializedName("SendSignConfirmation")
    private boolean sendSignConfirmation;
    @SerializedName("Language")
    private String language;
    @SerializedName("ScribbleName")
    private String scribbleName;
    @SerializedName("ScribbleNameFixed")
    private boolean scribbleNameFixed;
    @SerializedName("Reference")
    private String reference;
    @SerializedName("ReturnUrl")
    private String returnUrl;

    /**
     * Constructor for Signer
     * @param eMail The e-mail address of the recipient, cannot be null.
     */
    public Signer(String eMail){
        if(eMail == null){
            throw new IllegalArgumentException("Email address must be filled.");
        }
        this.eMail = eMail;
    }

    /**
     * set the mobile phone number of the signer.
     * @param mobile The mobile phone number of the signer.
     * @return The modified signer object containing the mobile number.
     */
    public Signer mobile(String mobile){
        if(!mobile.matches("\\+[\\d]{11}")){
            throw new IllegalArgumentException("The mobile number format must contain country prefix (e.g. +31612345678)");
        }
        this.mobile = mobile;
        return this;
    }

    /**
     * set value to require that the signer has to draw a scribble.
     * @param required true if required that the signer has to draw a scribble.
     * @return The modified signer object containing the requireScribble variable
     */
    public Signer requireScribble(boolean required){
        this.requireScribble = required;
        return this;
    }

    /**
     * set value to require that the signer has to verify by e-mail.
     * <p>Note: This is not implemented yet on the server side</p>
     * @param required true if required that the signer has verify by e-mail
     * @return The modified signer object containing the requireEmailVerification variable
     */
    public Signer requireEmailVerification(boolean required){
        LOGGER.warn("At the time of developing email-verification was not implemented yet server-side. Please look at https://api.signhost.com/Help/Api/POST-api-transaction for an up to date state");
        this.requireEmailVerification = required;
        return this;
    }

    /**
     * set value to require that the signer has to verify by sms.
     * @param required true if required that the signer has verify by sms
     * @return The modified signer object containing the requireSMSVerification variable
     */
    public Signer requireSMSVerification(boolean required){
        this.requireSMSVerification = required;
        return this;
    }

    /**
     * Send a message to the recipients email address asking him/her to sign.
     * @param enabled  true if we need to send an email, false otherwise
     * @param signRequestMessage The message to be included in the email
     * @return The modified signer object containing the SendSignRequest / SignRequestMessage variable
     */
    public Signer sendSignRequest(boolean enabled, String signRequestMessage){
        this.sendSignRequest = enabled;
        this.signRequestMessage = signRequestMessage;
        return this;
    }

    /**
     * Send a message to the recipients email address containing his sign-confirmation
     * @param sendSignConfirmation true if we need to send an email, false otherwise
     * @return The modified signer object containing the SendSignConfirmation variable
     */
    public Signer sendSignConfirmation(boolean sendSignConfirmation){
        this.sendSignConfirmation = sendSignConfirmation;
        return this;
    }

    /**
     * Set the language of the receiving user.
     * <p>Note: only de-DE, en-US, es-ES, fr-FR, it-IT and nl-NL are allowed.</p>
     * @param language the language of the receiving user
     * @return The modified signer object with the specified language setting
     */
    public Signer language(String language){
        this.language = language;
        return this;
    }

    /**
     * Set the name of the signer, this will be pre filled in the scribble form
     * @param name The scribble name
     * @param fixed True if the name is fixed, false if it can be changed by the signer
     * @return The modified signer object containing the scribble name
     */
    public Signer scribbleName(String name, boolean fixed){
        this.scribbleName = name;
        this.scribbleNameFixed = fixed;
        return this;
    }

    /**
     * Set the reference of the signer
     * @param reference The reference of the signer
     * @return The modified signer object containing the reference of the signer
     */
    public Signer reference(String reference){
        this.reference = reference;
        return this;
    }

    /**
     * Set the returnUrl
     * The returnUrl is the url to redirect the user to after signing, rejecting or
     * cancelling the signRequest.
     * @param returnUrl The url to redirect the user to after signing, rejecting or cancelling.
     * @return The modified signer object containing the returnUrl
     */
    public Signer returnUrl(String returnUrl){
        this.returnUrl = returnUrl;
        return this;
    }


    /* GENERATED GETTERS */

    public String getEMail() {
        return eMail;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isRequireScribble() {
        return requireScribble;
    }

    public boolean isRequireEmailVerification() {
        return requireEmailVerification;
    }

    public boolean isRequireSMSVerification() {
        return requireSMSVerification;
    }

    public boolean isSendSignRequest() {
        return sendSignRequest;
    }

    public String getSignRequestMessage() {
        return signRequestMessage;
    }

    public boolean isSendSignConfirmation() {
        return sendSignConfirmation;
    }

    public String getLanguage() {
        return language;
    }

    public String getScribbleName() {
        return scribbleName;
    }

    public boolean isScribbleNameFixed() {
        return scribbleNameFixed;
    }

    public String getReference() {
        return reference;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    /** GENERATED TO-STRING **/
    @Override
    public String toString() {
        return "Signer{" +
                "eMail='" + eMail + '\'' +
                ", mobile='" + mobile + '\'' +
                ", requireScribble=" + requireScribble +
                ", requireEmailVerification=" + requireEmailVerification +
                ", requireSMSVerification=" + requireSMSVerification +
                ", sendSignRequest=" + sendSignRequest +
                ", signRequestMessage='" + signRequestMessage + '\'' +
                ", sendSignConfirmation=" + sendSignConfirmation +
                ", language='" + language + '\'' +
                ", scribbleName='" + scribbleName + '\'' +
                ", scribbleNameFixed=" + scribbleNameFixed +
                ", reference='" + reference + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
