package com.translated.lara.translator;

public class DetectResult {
    private final String language;
    private final String contentType;

    public DetectResult(String language, String contentType) {
        this.language = language;
        this.contentType = contentType;
    }

    public String getLanguage() {
        return language;
    }

    public String getContentType() {
        return contentType;
    }
}
