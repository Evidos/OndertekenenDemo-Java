package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
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

    @Builder
    private IDINVerification(String accountHolderName, String accountHolderAddress1, String accountHolderAddress2, String accountHolderDateOfBirth) {
        super(VerificationType.IDIN);
        this.accountHolderName = accountHolderName;
        this.accountHolderAddress1 = accountHolderAddress1;
        this.accountHolderAddress2 = accountHolderAddress2;
        this.accountHolderDateOfBirth = accountHolderDateOfBirth;
    }

}
