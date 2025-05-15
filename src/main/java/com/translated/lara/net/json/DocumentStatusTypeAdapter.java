package com.translated.lara.net.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.translated.lara.translator.Document;

import java.lang.reflect.Type;

public class DocumentStatusTypeAdapter implements JsonDeserializer<Document.Status> {
    @Override
    public Document.Status deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            return Document.Status.valueOf(jsonElement.getAsString().toUpperCase());
        } else {
            return null;
        }
    }
}
