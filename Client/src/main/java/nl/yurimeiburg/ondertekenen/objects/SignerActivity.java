package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Data class containing Activity parameters, following:
 * https://api.signhost.com/Help/Api/GET-api-transaction-transactionId
 */
public class SignerActivity {
    @SerializedName("Id")
    private String id;
    @SerializedName("Code")
    private SignerActivityCode signerActivityCode;
    @SerializedName("Activity")
    private String activity;
    @SerializedName("CreatedDateTime")
    private String createdDateTime;



    /* Generated getters/setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SignerActivityCode getSignerActivityCode() {
        return signerActivityCode;
    }

    public void setSignerActivityCode(SignerActivityCode signerActivityCode) {
        this.signerActivityCode = signerActivityCode;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "SignerActivity{" +
                "id='" + id + '\'' +
                ", signerActivityCode=" + signerActivityCode +
                ", activity='" + activity + '\'' +
                ", createdDateTime='" + createdDateTime + '\'' +
                '}';
    }
}
