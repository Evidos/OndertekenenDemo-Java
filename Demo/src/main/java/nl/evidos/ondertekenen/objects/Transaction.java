package nl.evidos.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Data class containing Transaction parameters, following:
 * https://api.signhost.com/Help/Api/POST-api-transaction
 * @author Yuri Meiburg
 */
public class Transaction implements JSONModelObject {
    @SerializedName("Id")
    private String id =null;
    @SerializedName("Status")
    private TransactionStatus status;
    @SerializedName("File")
    private FileInfo file;
    @SerializedName("Seal")
    private boolean seal;
    @SerializedName("Signers")
    private Signer[] signers;
    @SerializedName("Reference")
    private String reference;
    @SerializedName("PostbackUrl")
    private String postbackUrl;
    @SerializedName("SendEmailNotifications")
    private boolean sendEmailNotifications;
    @SerializedName("DaysToExpire")
    private int daysToExpire;
    @SerializedName("SignRequestMode")
    private int signRequestMode;
    @SerializedName("DaysToRemind")
    private int daysToRemind;
    @SerializedName("CreatedDateTime")
    private String createdDateTime;
    @SerializedName("ModifiedDateTime")
    private String modifiedDateTime;
    @SerializedName("CanceledDateTime")
    private String canceledDateTime;

    public Transaction (FileInfo fileInfo, Signer[] signers){
        this.file = fileInfo;
        this.signers = signers;
    }

    /**
     * seal the document before sending to the signers
     * @param seal True if the document should be sealed.
     * @return The modified transaction object containing the sealing preferences
     */
    public Transaction seal(boolean seal){
        this.seal = seal;
        return this;
    }

    /**
     * Set the transaction reference
     * @param reference Your transaction reference
     * @return The modified transaction object containing the reference
     */
    public Transaction reference(String reference){
        this.reference = reference;
        return this;
    }

    /**
     * Set the url to postback the status updates
     * @param url The postback url
     * @return The modified transaction object containing the postback url
     */
    public Transaction postbackUrl(String url){
        this.postbackUrl = url;
        return this;
    }

    /**
     * Set whether or not e-mail notifications should be sent to the sender
     * @param enabled True if notifications should be anbled
     * @return The modified transaction object containing the e-mail notification preferences
     */
    public Transaction sendEmailNotifications(boolean enabled){
        this.sendEmailNotifications = enabled;
        return this;
    }

    /**
     * Set the amount of days before expiration. Max 90 days.
     * @param days The number of days before expiration
     * @return The modified transaction object containing the expiration days
     */
    public Transaction daysToExpire(int days){
        this.daysToExpire = days;
        return this;
    }

    /**
     * Set the Sign Request Mode.
     * @param signRequestMode 1 for sending at once, 2 for sequential
     * @return The modified transaction object containing the sign request mode
     */
    public Transaction signRequestMode(int signRequestMode){
        this.signRequestMode = signRequestMode;
        return this;
    }

    /**
     * Set the amount of days before reminding signers, set to -1 to disable.
     * @param days The number of days before reminding signers
     * @return The modified transaction object containing the reminder days
     */
    public Transaction daysToReminder(int days){
        this.daysToRemind = days;
        return this;
    }

    /* GENERATED GETTERS */
    public String getId() {
        return id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public String getModifiedDateTime() {
        return modifiedDateTime;
    }

    public String getCanceledDateTime() {
        return canceledDateTime;
    }

    public FileInfo getFile() {
        return file;
    }

    public boolean isSeal() {
        return seal;
    }

    public Signer[] getSigners() {
        return signers;
    }

    public String getReference() {
        return reference;
    }

    public String getPostbackUrl() {
        return postbackUrl;
    }

    public boolean isSendEmailNotifications() {
        return sendEmailNotifications;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public int getSignRequestMode() {
        return signRequestMode;
    }

    public int getDaysToRemind() {
        return daysToRemind;
    }

    /** GENERATED TOSTRING **/
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", file=" + file +
                ", seal=" + seal +
                ", signers=" + Arrays.toString(signers) +
                ", reference='" + reference + '\'' +
                ", postbackUrl='" + postbackUrl + '\'' +
                ", sendEmailNotifications=" + sendEmailNotifications +
                ", daysToExpire=" + daysToExpire +
                ", signRequestMode=" + signRequestMode +
                ", daysToRemind=" + daysToRemind +
                '}';
    }
}
