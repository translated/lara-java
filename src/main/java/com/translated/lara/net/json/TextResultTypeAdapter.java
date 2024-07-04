package com.translated.lara.net.json;

import com.google.gson.*;
import com.translated.lara.translator.TextBlock;
import com.translated.lara.translator.TextResult;

import java.lang.reflect.Type;

public class TextResultTypeAdapter implements JsonDeserializer<TextResult> {

    @Override
    public TextResult deserialize(JsonElement jElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jResult = jElement.getAsJsonObject();

        String contentType = jResult.get("content_type").getAsString();
        String sourceLanguage = jResult.get("source_language").getAsString();
        String[] adaptedTo = context.deserialize(jResult.get("adapted_to"), String[].class);

        JsonElement jTranslation = jResult.get("translation");
        if (jTranslation != null && !jTranslation.isJsonNull()) {
            if (jTranslation.isJsonArray()) {
                JsonArray jTranslationArray = jTranslation.getAsJsonArray();

                if (jTranslationArray.isEmpty()) {
                    return new TextResult(contentType, sourceLanguage, new String[0], adaptedTo);
                } else {
                    if (jTranslationArray.get(0).isJsonObject()) {
                        TextBlock[] blocks = context.deserialize(jTranslationArray, TextBlock[].class);
                        return new TextResult(contentType, sourceLanguage, blocks, adaptedTo);
                    } else {
                        String[] translations = context.deserialize(jTranslationArray, String[].class);
                        return new TextResult(contentType, sourceLanguage, translations, adaptedTo);
                    }
                }
            } else {
                return new TextResult(contentType, sourceLanguage, jTranslation.getAsString(), adaptedTo);
            }
        } else {
            return new TextResult(contentType, sourceLanguage, (String) null, adaptedTo);
        }

    }
}
