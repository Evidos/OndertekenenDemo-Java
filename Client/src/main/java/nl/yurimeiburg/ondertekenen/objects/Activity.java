package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Data class containing Activity parameters, following:
 * https://api.signhost.com/Help/Api/GET-api-transaction-transactionId
 */
@Data
public class Activity {
    @SerializedName("Id")
    private String id;
    @SerializedName("Code")
    private ActivityCode activityCode;
    @SerializedName("Activity")
    private String activity;
    @SerializedName("CreatedDateTime")
    private String createdDateTime;

}
