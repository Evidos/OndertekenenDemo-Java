package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormSets {
    @SerializedName("FormSets")
    private String[] formSets;
}
