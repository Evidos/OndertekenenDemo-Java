package nl.evidos.ondertekenen.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nl.evidos.ondertekenen.objects.SignerActivityCode;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to SignerActivityCode
 * This adapter is used by GSON to convert JSON to SignerActivityCode.
 * @see nl.evidos.ondertekenen.objects.SignerActivityCode
 */
public class SignerActivityCodeDeserializer implements JsonDeserializer<SignerActivityCode>{

    /**
     * {@inheritDoc}
     */
    @Override
    public SignerActivityCode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return SignerActivityCode.toSignerActivityCode(jsonElement.getAsInt());
    }
}
