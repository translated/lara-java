package com.translated.lara.translator;

import com.translated.lara.errors.LaraException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageTranslator {
    private final LaraClient client;

    public ImageTranslator(LaraClient client) {
        this.client = client;
    }

    public InputStream translate(File image, String target) throws LaraException {
        return translate(image, null, target, null);
    }

    public InputStream translate(File image, String source, String target) throws LaraException {
        return translate(image, source, target, null);
    }

    public InputStream translate(File image, String source, String target, ImageTranslateOptions options) throws LaraException {
        HttpParams<Object> params = (options == null ? new HttpParams<>() : options.toParams())
                .set("source", source)
                .set("target", target);

        Map<String, File> files = new HttpParams<File>()
                .set("image", image)
                .build();

        Map<String, String> headers = new HashMap<>();
        if (options != null && Boolean.TRUE.equals(options.getNoTrace())) {
            headers.put("X-No-Trace", "true");
        }


        return client.postAndGetInputStream("/v2/images/translate", params.build(), files, headers);
    }

    public ImageTextResult translateText(File image, String target) throws LaraException {
        return translateText(image, null, target, null);
    }

    public ImageTextResult translateText(File image, String source, String target) throws LaraException {
        return translateText(image, source, target, null);
    }

    public ImageTextResult translateText(File image, String source, String target, ImageTextTranslateOptions options) throws LaraException {
        HttpParams<Object> params = options == null ? new HttpParams<>() : options.toParams();

        if (source != null) {
            params.set("source", source);
        }
        params.set("target", target);

        Map<String, File> files = new HttpParams<File>()
                .set("image", image)
                .build();

        Map<String, String> headers = new HashMap<>();
        if (options != null && Boolean.TRUE.equals(options.getNoTrace())) {
            headers.put("X-No-Trace", "true");
        }

        return client.post("/v2/images/translate-text", params.build(), files, headers).as(ImageTextResult.class);
    }
}
