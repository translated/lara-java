package com.translated.lara.net.json;

import com.google.gson.*;
import com.translated.lara.translator.TextBlock;
import com.translated.lara.translator.TextResult;

import java.lang.reflect.Type;

public class TextResultValueTypeAdapter implements JsonDeserializer<TextResult.Value> {

    @Override
    public TextResult.Value deserialize(JsonElement jElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jElement != null && !jElement.isJsonNull()) {
            if (jElement.isJsonArray()) {
                JsonArray jTranslationArray = jElement.getAsJsonArray();

                if (jTranslationArray.isEmpty()) {
                    return new TextResult.Value(new String[0]);
                } else {
                    if (jTranslationArray.get(0).isJsonObject()) {
                        TextBlock[] blocks = context.deserialize(jTranslationArray, TextBlock[].class);
                        return new TextResult.Value(blocks);
                    } else {
                        String[] translations = context.deserialize(jTranslationArray, String[].class);
                        return new TextResult.Value(translations);
                    }
                }
            } else {
                return new TextResult.Value(jElement.getAsString());
            }
        } else {
            return new TextResult.Value((String) null);
        }

    }
}
