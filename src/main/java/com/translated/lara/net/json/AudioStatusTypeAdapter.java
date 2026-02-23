package com.translated.lara.net.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.translated.lara.translator.Audio;

import java.lang.reflect.Type;

public class AudioStatusTypeAdapter implements JsonDeserializer<Audio.Status> {
    @Override
    public Audio.Status deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            return Audio.Status.valueOf(jsonElement.getAsString().toUpperCase());
        } else {
            return null;
        }
    }
}

