package nl.yurimeiburg.ondertekenen.gson;

import com.google.gson.*;
import nl.yurimeiburg.ondertekenen.objects.TransactionStatus;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to TransactionStatus
 * This adapter is used by GSON to convert JSON to TransactionStatus.
 * @see nl.yurimeiburg.ondertekenen.objects.TransactionStatus
 */
public class TransactionStatusDeserializer implements JsonDeserializer<TransactionStatus>{

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionStatus deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return TransactionStatus.toTransactionStatus(jsonElement.getAsInt());
    }
}
