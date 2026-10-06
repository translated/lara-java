package com.translated.lara.net.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.translated.lara.translator.PermissionMask;

import java.io.IOException;

/** Serializes permission masks as their raw API string without discarding unknown values. */
public final class PermissionMaskTypeAdapter extends TypeAdapter<PermissionMask> {
    @Override
    public void write(JsonWriter writer, PermissionMask permission) throws IOException {
        if (permission == null) {
            writer.nullValue();
            return;
        }
        writer.value(permission.getValue());
    }

    @Override
    public PermissionMask read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return PermissionMask.fromValue(reader.nextString());
    }
}
