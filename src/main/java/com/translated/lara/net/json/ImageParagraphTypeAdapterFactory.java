package com.translated.lara.net.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.translated.lara.translator.ImageLayoutParagraph;
import com.translated.lara.translator.ImageParagraph;

import java.io.IOException;

/** Selects the paragraph subtype from the layout actually returned by the API. */
public class ImageParagraphTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != ImageParagraph.class) return null;

        TypeAdapter<ImageParagraph> textAdapter = gson.getDelegateAdapter(this, TypeToken.get(ImageParagraph.class));
        TypeAdapter<ImageLayoutParagraph> layoutAdapter = gson.getAdapter(ImageLayoutParagraph.class);
        return (TypeAdapter<T>) new TypeAdapter<ImageParagraph>() {
            @Override
            public ImageParagraph read(JsonReader in) throws IOException {
                JsonElement json = JsonParser.parseReader(in);
                JsonObject paragraph = json.getAsJsonObject();
                if (hasValue(paragraph, "bbox") && hasValue(paragraph, "lines_bboxes")
                        && hasValue(paragraph, "text_info") && hasValue(paragraph, "alignment")) {
                    return layoutAdapter.fromJsonTree(json);
                }
                return textAdapter.fromJsonTree(json);
            }

            @Override
            public void write(JsonWriter out, ImageParagraph paragraph) throws IOException {
                if (paragraph instanceof ImageLayoutParagraph) {
                    layoutAdapter.write(out, (ImageLayoutParagraph) paragraph);
                } else {
                    textAdapter.write(out, paragraph);
                }
            }
        }.nullSafe();
    }

    private static boolean hasValue(JsonObject object, String field) {
        return object.has(field) && !object.get(field).isJsonNull();
    }
}
