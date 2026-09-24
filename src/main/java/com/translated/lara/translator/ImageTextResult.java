package com.translated.lara.translator;

import java.util.List;

public class ImageTextResult {

    private final String sourceLanguage;
    private final List<ImageParagraph> paragraphs;
    private final List<String> adaptedTo;
    private final List<String> glossaries;

    public ImageTextResult(String sourceLanguage, List<ImageParagraph> paragraphs, List<String> adaptedTo, List<String> glossaries) {
        this.sourceLanguage = sourceLanguage;
        this.paragraphs = paragraphs;
        this.adaptedTo = adaptedTo;
        this.glossaries = glossaries;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    /**
     * @return translated paragraphs; when {@code includeLayout=true}, every entry is an
     * {@link ImageLayoutParagraph} instance with complete layout metadata
     */
    public List<ImageParagraph> getParagraphs() {
        return paragraphs;
    }

    public List<String> getAdaptedTo() {
        return adaptedTo;
    }

    public List<String> getGlossaries() {
        return glossaries;
    }
}
