package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Verification {

    @SerializedName("Type")
    private VerificationType type;
}
