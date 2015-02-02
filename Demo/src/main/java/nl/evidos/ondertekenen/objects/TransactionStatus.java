package nl.evidos.ondertekenen.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration containing all possible statuses for a transaction to be in.
 * Created by Yuri Meiburg on 23-1-2015.
 */
public enum TransactionStatus {
    UNKNOWN(-1),
    WAITING_FOR_DOCUMENT(5),
    WAITING_FOR_SIGNER(10),
    IN_PROGRESS(20),
    SIGNED(30),
    REJECTED(40),
    EXPIRED(50),
    CANCELLED(60),
    FAILED(70);

    private static Map<Integer, TransactionStatus> values = new HashMap<>();
    private int statusCode = -1;

    static {
        for (TransactionStatus transactionStatus : values()) {
            values.put(transactionStatus.value(), transactionStatus);
        }
    }

    TransactionStatus(int status) {
        this.statusCode = status;
    }

    public int value() {
        return statusCode;
    }

    public static TransactionStatus toTransactionStatus(int statusCode) {
        return values.containsKey(statusCode) ? values.get(statusCode) : UNKNOWN;
    }

    @Override
    public String toString() {
        return "TransactionStatus{" +
                "statusCode=" + statusCode +
                ", name=" + name() +
                '}';
    }
}
