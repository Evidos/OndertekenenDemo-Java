package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Verification {

    @SerializedName("Type")
    private VerificationType type;
}
