package nl.yurimeiburg.ondertekenen.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nl.yurimeiburg.ondertekenen.objects.FormSetType;
import nl.yurimeiburg.ondertekenen.objects.LinkType;

import java.lang.reflect.Type;

/**
 * Adapter to convert JSON Objects to LinkType
 * This adapter is used by GSON to convert JSON to LinkType.
 *
 * @see FormSetType
 */
public class LinkTypeDeserializer implements JsonDeserializer<LinkType> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LinkType.toLinkCode(jsonElement.getAsString());
    }
}
