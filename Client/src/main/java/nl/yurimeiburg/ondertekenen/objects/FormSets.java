package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/**
 * Created by yuri on 2/3/2017.
 */
@Data
@Builder
public class FormSets {
    @SerializedName("FormSets")
    private String [] formSets;
}
