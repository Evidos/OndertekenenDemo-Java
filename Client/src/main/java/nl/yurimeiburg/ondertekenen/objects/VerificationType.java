package nl.yurimeiburg.ondertekenen.objects;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum VerificationType {
    IDEAL("iDeal"),
    IDIN("iDIN"),
    UNKNOWN("Unknown");


    private static Map<String, VerificationType> values = new HashMap<>();
    private String code = "";

    static {
        for (VerificationType verificationType : values()) {
            values.put(verificationType.value(), verificationType);
        }
    }

    VerificationType(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }

    public static VerificationType toVerificationCode(String statusCode) {
        return values.containsKey(statusCode) ? values.get(statusCode) : UNKNOWN;
    }

}

