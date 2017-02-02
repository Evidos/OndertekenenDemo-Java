package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Link {
    @SerializedName("Rel")
    private LinkType rel;
    @SerializedName("Type")
    private String type;
    @SerializedName("Link")
    private String link;
}
