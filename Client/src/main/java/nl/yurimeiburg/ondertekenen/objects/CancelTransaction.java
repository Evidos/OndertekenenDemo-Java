package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

/**
 * The body for a 'Cancel Transaction' action. (see: https://api.signhost.com/Help/Api/DELETE-api-transaction-transactionId)
 * @author Yuri Meiburg
 */
public class CancelTransaction {
    @SerializedName("SendNotification")
    private boolean sendNotification;

    @SerializedName("Reason")
    private String reason= null;

    /**
     * Construct a CancelTransaction object
     * @param sendNotification True if sender and recipient should be notified of the cancel action
     * @param reason Reason to mention for cancelling the transaction.
     */
    public CancelTransaction(boolean sendNotification, String reason){
        this.sendNotification=sendNotification;
        this.reason=reason;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public String getReason() {
        return reason;
    }
}
