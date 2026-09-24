package com.translated.lara.translator;

import com.translated.lara.errors.LaraException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /** Renders supplied translations using the API default model, {@code generative_fast}. */
    public InputStream renderTranslated(File image, String target, List<? extends ImageParagraph> paragraphs) throws LaraException {
        return renderTranslatedRequest(image, null, target, paragraphs, null, null);
    }

    /** Renders supplied translations using the API default model, {@code generative_fast}. */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageParagraph> paragraphs) throws LaraException {
        return renderTranslatedRequest(image, source, target, paragraphs, null, null);
    }

    /**
     * Renders text-only or complete layout paragraphs using the API default model,
     * {@code generative_fast}, with request tracing control.
     *
     * @param image the original image file
     * @param source source language, or {@code null} to omit it
     * @param target target language
     * @param paragraphs source text, supplied translations, and optional complete layout
     * @param noTrace {@code true} to send {@code X-No-Trace: true}; {@code false} to omit the header
     * @return rendered image stream; the caller must close it
     * @throws LaraException if the request fails
     */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageParagraph> paragraphs,
                                        boolean noTrace) throws LaraException {
        return renderTranslatedRequest(image, source, target, paragraphs, null, noTrace);
    }

    /**
     * Renders supplied translations onto the original image without translating them again.
     * This overload accepts every image model and requires layout paragraphs, including when
     * the model is only known at runtime. To render text-only paragraphs with a generative model,
     * use the overload accepting {@link ImageGenerativeModel}, or omit the model.
     *
     * @param image the original image file
     * @param source source language, or {@code null} to omit it
     * @param target target language
     * @param paragraphs source text, supplied translations, and complete layout
     * @param model rendering model
     * @return rendered image stream; the caller must close it
     * @throws LaraException if the request fails
     */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageLayoutParagraph> paragraphs,
                                        ImageTranslationModel model) throws LaraException {
        return renderTranslated(image, source, target, paragraphs, model, null);
    }

    /**
     * Renders complete layout paragraphs with optional request tracing control.
     *
     * @param image the original image file
     * @param source source language, or {@code null} to omit it
     * @param target target language
     * @param paragraphs source text, supplied translations, and complete layout
     * @param model rendering model, or {@code null} to use the API default
     * @param noTrace {@code true} to send {@code X-No-Trace: true}; {@code false} or {@code null} to omit the header
     * @return rendered image stream; the caller must close it
     * @throws LaraException if the request fails
     */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageLayoutParagraph> paragraphs,
                                        ImageTranslationModel model, Boolean noTrace) throws LaraException {
        return renderTranslatedRequest(image, source, target, paragraphs, ImageTranslationModel.toString(model), noTrace);
    }

    /**
     * Renders supplied translations with a generative model. Text-only and complete layout
     * paragraphs are accepted. Omit the model argument to use the API default, {@code generative_fast}.
     * The API validates paragraph contents; memory and glossary matches are excluded from the request.
     *
     * @param image the original image file
     * @param source source language, or {@code null} to omit it
     * @param target target language
     * @param paragraphs source text, supplied translations, and optional complete layout
     * @param model generative rendering model
     * @return rendered image stream; the caller must close it
     * @throws LaraException if the request fails
     */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageParagraph> paragraphs,
                                        ImageGenerativeModel model) throws LaraException {
        return renderTranslated(image, source, target, paragraphs, model, null);
    }

    /**
     * Renders text-only or complete layout paragraphs with optional request tracing control.
     *
     * @param image the original image file
     * @param source source language, or {@code null} to omit it
     * @param target target language
     * @param paragraphs source text, supplied translations, and optional complete layout
     * @param model generative rendering model, or {@code null} to use the API default
     * @param noTrace {@code true} to send {@code X-No-Trace: true}; {@code false} or {@code null} to omit the header
     * @return rendered image stream; the caller must close it
     * @throws LaraException if the request fails
     */
    public InputStream renderTranslated(File image, String source, String target, List<? extends ImageParagraph> paragraphs,
                                        ImageGenerativeModel model, Boolean noTrace) throws LaraException {
        return renderTranslatedRequest(image, source, target, paragraphs, ImageGenerativeModel.toString(model), noTrace);
    }

    private InputStream renderTranslatedRequest(File image, String source, String target, List<? extends ImageParagraph> paragraphs,
                                               String model, Boolean noTrace) throws LaraException {
        // Select rendering fields instead of sending verbose translation matches back to the API.
        List<Map<String, Object>> renderParagraphs = new ArrayList<>(paragraphs.size());
        for (ImageParagraph paragraph : paragraphs) {
            HttpParams<Object> fields = new HttpParams<Object>()
                    .set("text", paragraph.getText())
                    .set("translation", paragraph.getTranslation());
            if (paragraph instanceof ImageLayoutParagraph) {
                ImageLayoutParagraph layout = (ImageLayoutParagraph) paragraph;
                fields.set("bbox", layout.getBbox())
                        .set("lines_bboxes", layout.getLinesBboxes())
                        .set("text_info", layout.getTextInfo())
                        .set("alignment", layout.getAlignment());
            }
            renderParagraphs.add(fields.build());
        }

        Map<String, Object> params = new HttpParams<Object>()
                .set("source", source)
                .set("target", target)
                .set("paragraphs", renderParagraphs)
                .set("model", model)
                .build();
        Map<String, File> files = new HttpParams<File>()
                .set("image", image)
                .build();

        Map<String, String> headers = new HashMap<>();
        if (Boolean.TRUE.equals(noTrace)) {
            headers.put("X-No-Trace", "true");
        }

        return client.postAndGetInputStream("/v2/images/render-translated", params, files, headers);
    }

    public ImageTextResult translateText(File image, String target) throws LaraException {
        return translateText(image, null, target, null);
    }

    public ImageTextResult translateText(File image, String source, String target) throws LaraException {
        return translateText(image, source, target, null);
    }

    /**
     * Extracts and translates image text. The API returns layout only when
     * {@link ImageTextTranslateOptions#setIncludeLayout(Boolean)} is explicitly set to {@code true}.
     * With the option omitted, {@code null}, or {@code false}, returned entries are {@link ImageParagraph}.
     * With {@code true}, every entry is an {@link ImageLayoutParagraph} with the complete
     * metadata required by classic rendering models.
     * The verbose option independently controls memory and glossary matches.
     */
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
