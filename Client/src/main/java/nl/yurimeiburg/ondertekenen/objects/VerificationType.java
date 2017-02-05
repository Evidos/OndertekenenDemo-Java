package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public enum VerificationType {
    @SerializedName("iDEAL")
    IDEAL("iDEAL"),
    @SerializedName("iDIN")
    IDIN("iDIN"),
    UNKNOWN("Unknown");

    private String value;

    VerificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

