package com.translated.lara.translator;

/** An HTTPS URL or base64 data URL containing a PNG, JPEG or WebP image. */
public class ImageContext {
    private final String url;

    public ImageContext(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
