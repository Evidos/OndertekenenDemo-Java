package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
public class IDEALVerification extends Verification {
    @SerializedName("Iban")
    String iban;
    @SerializedName("AccountHolderName")
    String accountHolderName;
    @SerializedName("AccountHolderCity")
    String accountHolderCity;

    @Builder
    private IDEALVerification(String iban, String accountHolderName, String accountHolderCity) {
        super(VerificationType.IDEAL);
        this.iban = iban;
        this.accountHolderName = accountHolderName;
        this.accountHolderCity = accountHolderCity;
    }
}