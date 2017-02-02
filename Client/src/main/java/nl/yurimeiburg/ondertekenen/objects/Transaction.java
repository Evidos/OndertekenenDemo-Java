package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data class containing Transaction parameters, following:
 * https://evidos.github.io/endpoints/#%23/definitions/Transaction
 *
 * @author Yuri Meiburg
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends JSONModelObject {
    @SerializedName("Id")
    private String id = null;
    @SerializedName("Files")
    private Map<String, FileEntry> files;
    @SerializedName("Seal")
    private boolean seal = true;
    @SerializedName("Signers")
    private Signer[] signers;
    @SerializedName("Receivers")
    private Receiver[] receivers;
    @SerializedName("Reference")
    private String reference;
    @SerializedName("PostbackUrl")
    private String postbackUrl;
    @SerializedName("SignRequestMode")
    private int signRequestMode = 2;
    @SerializedName("DaysToExpire")
    private int daysToExpire = 60;
    @SerializedName("SendEmailNotifications")
    private boolean sendEmailNotifications = false;
    @SerializedName("Status")
    private TransactionStatus status;
    @SerializedName("Context")
    private Object context;

}
