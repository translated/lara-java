package com.translated.lara.translator;

public class StyleguideChange {

    private final String id;
    private final String originalTranslation;
    private final String refinedTranslation;
    private final String explanation;

    public StyleguideChange(String id, String originalTranslation, String refinedTranslation, String explanation) {
        this.id = id;
        this.originalTranslation = originalTranslation;
        this.refinedTranslation = refinedTranslation;
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public String getOriginalTranslation() {
        return originalTranslation;
    }

    public String getRefinedTranslation() {
        return refinedTranslation;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public String toString() {
        return "StyleguideChange{id='" + id + "', explanation='" + explanation + "'}";
    }
}
