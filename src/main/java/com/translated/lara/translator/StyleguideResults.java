package com.translated.lara.translator;

import java.util.List;

public class StyleguideResults {

    private final TextResult.Value originalTranslation;
    private final List<StyleguideChange> changes;

    public StyleguideResults(TextResult.Value originalTranslation, List<StyleguideChange> changes) {
        this.originalTranslation = originalTranslation;
        this.changes = changes;
    }

    public String getOriginalTranslation() {
        return originalTranslation != null ? originalTranslation.getTranslation() : null;
    }

    public List<String> getOriginalTranslations() {
        return originalTranslation != null ? originalTranslation.getTranslations() : null;
    }

    public List<TextBlock> getOriginalTranslationBlocks() {
        return originalTranslation != null ? originalTranslation.getTranslationBlocks() : null;
    }

    public List<StyleguideChange> getChanges() {
        return changes;
    }

    @Override
    public String toString() {
        return "StyleguideResults{changes=" + (changes != null ? changes.size() : 0) + "}";
    }
}
