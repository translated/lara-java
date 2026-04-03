package com.translated.lara.translator;

public enum ContentType {

    TEXT("text/plain"),
    HTML("text/html"),
    XML("text/xml"),
    XLIFF("application/xliff+xml");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ContentType contentType) {
        return contentType != null ? contentType.toString() : null;
    }
}
