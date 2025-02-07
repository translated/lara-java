package com.translated.lara.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TextResult {

    private final String contentType;
    private final String sourceLanguage;
    private final List<String> adaptedTo;

    private final String translation;
    private final List<String> translations;
    private final List<TextBlock> translationBlocks;

    private final Map<String, Object> extras;

    public TextResult(String contentType, String sourceLanguage, String translation, String[] adaptedTo, Map<String, Object> extras) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));

        this.translation = translation;
        this.translations = null;
        this.translationBlocks = null;

        this.extras = extras;
    }

    public TextResult(String contentType, String sourceLanguage, String[] translation, String[] adaptedTo, Map<String, Object> extras) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));

        this.translation = null;
        this.translations = Collections.unmodifiableList(Arrays.asList(translation));
        this.translationBlocks = null;

        this.extras = extras;
    }

    public TextResult(String contentType, String sourceLanguage, TextBlock[] translation, String[] adaptedTo, Map<String, Object> extras) {
        this.contentType = contentType;
        this.sourceLanguage = sourceLanguage;
        this.adaptedTo = adaptedTo == null ? null : Collections.unmodifiableList(Arrays.asList(adaptedTo));

        this.translation = null;
        this.translations = null;
        this.translationBlocks = Collections.unmodifiableList(Arrays.asList(translation));

        this.extras = extras;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public List<String> getAdaptedTo() {
        return adaptedTo;
    }

    public String getTranslation() {
        if (translation != null)
            return translation;

        if (translations != null && !translations.isEmpty()) {
            if (translations.size() != 1)
                throw new IllegalStateException("Cannot get translation from multiple elements (" + translations.size() + ")");
            return translations.get(0);
        }

        if (translationBlocks != null && !translationBlocks.isEmpty()) {
            if (translationBlocks.size() != 1)
                throw new IllegalStateException("Cannot get translation from multiple elements (" + translationBlocks.size() + ")");
            return translationBlocks.get(0).getText();
        }

        return null;
    }

    public List<String> getTranslations() {
        if (translations != null)
            return translations;

        if (translation != null)
            return Collections.singletonList(translation);

        if (translationBlocks != null && !translationBlocks.isEmpty()) {
            List<String> result = new ArrayList<>(translationBlocks.size());
            for (TextBlock block : translationBlocks)
                result.add(block.getText());

            return result;
        }

        return null;
    }

    public List<TextBlock> getTranslationBlocks() {
        if (translationBlocks != null)
            return translationBlocks;

        if (translation != null)
            return Collections.singletonList(new TextBlock(translation));

        if (translations != null && !translations.isEmpty()) {
            List<TextBlock> result = new ArrayList<>(translations.size());
            for (String translation : translations)
                result.add(new TextBlock(translation));

            return result;
        }

        return null;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    @Override
    public String toString() {
        if (translation != null) return translation;
        if (translations != null) return translations.toString();
        if (translationBlocks != null) return translationBlocks.toString();
        return null;
    }

}
