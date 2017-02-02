package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IDINVerification extends Verification {
    @SerializedName("AccountHolderName")
    String accountHolderName;
    @SerializedName("AccountHolderAddress1")
    String accountHolderAddress1;
    @SerializedName("AccountHolderAddress2")
    String accountHolderAddress2;
    @SerializedName("AccountHolderDateOfBirth")
    String accountHolderDateOfBirth;
}
