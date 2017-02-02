package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Data class containing Activity parameters, following:
 * https://api.signhost.com/Help/Api/GET-api-transaction-transactionId
 */
@Data
public class Receiver {
    @SerializedName("Name")
    private String name;
    @SerializedName("Email")
    private String email;
    @SerializedName("Language")
    private String language = "nl-NL";
    @SerializedName("Message")
    private String message;
    @SerializedName("Reference")
    private String reference;

}
