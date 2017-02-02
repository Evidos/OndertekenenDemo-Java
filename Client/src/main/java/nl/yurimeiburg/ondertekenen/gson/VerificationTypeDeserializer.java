package nl.yurimeiburg.ondertekenen.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nl.yurimeiburg.ondertekenen.objects.FormSetType;
import nl.yurimeiburg.ondertekenen.objects.VerificationType;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to VerificationType
 * This adapter is used by GSON to convert JSON to VerificationType.
 *
 * @see FormSetType
 */
public class VerificationTypeDeserializer implements JsonDeserializer<VerificationType> {

    /**
     * {@inheritDoc}
     */
    @Override
    public VerificationType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return VerificationType.toVerificationCode(jsonElement.getAsString());
    }
}
