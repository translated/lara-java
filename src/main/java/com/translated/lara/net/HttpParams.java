package com.translated.lara.net;

import java.util.HashMap;
import java.util.Map;

public class HttpParams<V> {

    private final Map<String, V> values = new HashMap<>();

    public HttpParams<V> set(String key, V value) {
        if (value != null)
            values.put(key, value);
        return this;
    }

    public Map<String, V> build() {
        return values.isEmpty() ? null : values;
    }

}
