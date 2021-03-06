package nl.yurimeiburg.ondertekenen.objects;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum FormSetType {
    @SerializedName("Seal")
    SEAL("Seal"),
    @SerializedName("Signature")
    SIGNATURE("Signature"),
    @SerializedName("Check")
    CHECK("Check"),
    @SerializedName("SingleLine")
    SINGLE_LINE("SingleLine"),
    UNKNOWN("Unknown");


    private static Map<String, FormSetType> values = new HashMap<>();
    private String code = "";

    static {
        for (FormSetType formSetType : values()) {
            values.put(formSetType.value(), formSetType);
        }
    }

    FormSetType(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }

    public static FormSetType toFormSetType(String statusCode) {
        return values.containsKey(statusCode) ? values.get(statusCode) : UNKNOWN;
    }

}

