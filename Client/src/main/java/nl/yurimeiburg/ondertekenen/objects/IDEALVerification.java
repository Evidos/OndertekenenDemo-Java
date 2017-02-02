package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IDEALVerification extends Verification {
    @SerializedName("Iban")
    String iban;
    @SerializedName("AccountHolderName")
    String accountHolderName;
    @SerializedName("AccountHolderCity")
    String accountHolderCity;
}
