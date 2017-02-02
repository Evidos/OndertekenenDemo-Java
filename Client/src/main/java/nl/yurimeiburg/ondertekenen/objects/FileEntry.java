package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileEntry {
    @SerializedName("Links") // TODO update to correct type
    private Object[] links;
    @SerializedName("DisplayName")
    private String displayName;
}
