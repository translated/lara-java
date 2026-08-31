package com.translated.lara.net.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.translated.lara.translator.SharePermission;

import java.io.IOException;

/** Serializes share permissions as their raw API string without discarding unknown values. */
public final class SharePermissionTypeAdapter extends TypeAdapter<SharePermission> {
    @Override
    public void write(JsonWriter writer, SharePermission permission) throws IOException {
        if (permission == null) {
            writer.nullValue();
            return;
        }
        writer.value(permission.getValue());
    }

    @Override
    public SharePermission read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return SharePermission.fromValue(reader.nextString());
    }
}
