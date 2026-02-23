package com.translated.lara.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

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

    public String toQueryString() {
        if (values.isEmpty()) return "";
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, V> e : values.entrySet()) {
            String key = e.getKey();
            V value = e.getValue();
            if (key == null || value == null) continue;
            if (!isAllowed(value)) continue;
            try {
                String encKey = URLEncoder.encode(key, "UTF-8");
                String encValue = URLEncoder.encode(coerceValue(value), "UTF-8");
                joiner.add(encKey + "=" + encValue);
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 not supported", ex);
            }
        }
        return joiner.toString();
    }

    private boolean isAllowed(Object value) {
        return value instanceof CharSequence ||
                value instanceof Number ||
                value instanceof Boolean ||
                value instanceof Enum;
    }

    private String coerceValue(Object value) {
        if (value instanceof Enum<?>) return ((Enum<?>) value).name();
        return value.toString();
    }
}
