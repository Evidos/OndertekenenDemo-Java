package nl.yurimeiburg.ondertekenen.objects;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FileMetaData {
    @SerializedName("DisplayName")
    private String displayName;
    @SerializedName("Signers")
    private Map<String, FormSets> signers;
    @SerializedName("FormSets")
    private Map<String, Map<String, FormSetField>> formSets;

}
