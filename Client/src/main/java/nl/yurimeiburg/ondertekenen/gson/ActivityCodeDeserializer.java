package nl.yurimeiburg.ondertekenen.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nl.yurimeiburg.ondertekenen.objects.ActivityCode;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to SignerActivityCode
 * This adapter is used by GSON to convert JSON to SignerActivityCode.
 *
 * @see ActivityCode
 */
public class ActivityCodeDeserializer implements JsonDeserializer<ActivityCode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityCode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ActivityCode.toSignerActivityCode(jsonElement.getAsInt());
    }
}
