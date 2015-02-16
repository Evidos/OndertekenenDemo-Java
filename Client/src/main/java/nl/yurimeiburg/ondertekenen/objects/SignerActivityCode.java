package nl.yurimeiburg.ondertekenen.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration containing all possible Signer Activities.
 * Created by Yuri Meiburg on 23-1-2015.
 */
public enum SignerActivityCode {
    UNKNOWN(-1),
    INITIATION_SENT(101),
    RECEIVED(102),
    OPENED(103),
    REMINDER_SENT(104),
    CANCELLED(201),
    REJECTED(202),
    SIGNED(203),
    SIGNED_DOCUMENT_SENT(301),
    SIGNED_DOCUMENT_OPENED(302),
    SIGNED_DOCUMENT_DOWNLOADED(303),
    RECEIPT_SENT(401),
    RECEIPT_OPENED(402),
    RECEIPT_DOWNLOADED(403),
    FINISHED(500),
    DELETED(600),
    EXPIRED(700),
    FAILED(999);


    private static Map<Integer, SignerActivityCode> values = new HashMap<>();
    private int statusCode = -1;

    static {
        for (SignerActivityCode signerActivity : values()) {
            values.put(signerActivity.value(), signerActivity);
        }
    }

    SignerActivityCode(int status) {this.statusCode = status;}

    public int value() {
        return statusCode;
    }

    public static SignerActivityCode toSignerActivityCode(int statusCode) {
        return values.containsKey(statusCode) ? values.get(statusCode) : UNKNOWN;
    }

    @Override
    public String toString() {
        return "SignerActivity{" +
                "statusCode=" + statusCode +
                ", name=" + name() +
                '}';
    }
}

