package nl.yurimeiburg.ondertekenen.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nl.yurimeiburg.ondertekenen.objects.FormSetType;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to FormSetType
 * This adapter is used by GSON to convert JSON to FormSetType.
 *
 * @see FormSetType
 */
public class FormSetTypeDeserializer implements JsonDeserializer<FormSetType> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FormSetType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return FormSetType.toFormSetType(jsonElement.getAsString());
    }
}
