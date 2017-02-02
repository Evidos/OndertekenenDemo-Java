package nl.yurimeiburg.ondertekenen.objects;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum LinkType {
    FILE("file"),
    RECEIPT("receipt"),
    SIGNER_SIGN("signer.sign"),
    SIGNER_DOWNLOAD("signer.download"),
    UNKNOWN("Unknown");


    private static Map<String, LinkType> values = new HashMap<>();
    private String code = "";

    static {
        for (LinkType linkType : values()) {
            values.put(linkType.value(), linkType);
        }
    }

    LinkType(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }

    public static LinkType toLinkCode(String statusCode) {
        return values.containsKey(statusCode) ? values.get(statusCode) : UNKNOWN;
    }

}

