package com.translated.lara.net.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.translated.lara.translator.VoiceGender;

import java.lang.reflect.Type;

public class VoiceGenderTypeAdapter implements JsonDeserializer<VoiceGender> {
    @Override
    public VoiceGender deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            return VoiceGender.valueOf(jsonElement.getAsString().toUpperCase());
        } else {
            return null;
        }
    }
}
