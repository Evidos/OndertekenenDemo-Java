package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FormSetField {
    @SerializedName("Type")
    FormSetType type;
    @SerializedName("Location")
    Location location;
}
