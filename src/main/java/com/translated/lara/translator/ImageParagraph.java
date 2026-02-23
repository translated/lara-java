package com.translated.lara.translator;

import java.util.List;

public class ImageParagraph {

    private final String text;
    private final String translation;
    private final List<NGMemoryMatch> adaptedToMatches;
    private final List<NGGlossaryMatch> glossariesMatches;

    public ImageParagraph(String text, String translation, List<NGMemoryMatch> adaptedToMatches, List<NGGlossaryMatch> glossaryMatches) {
        this.text = text;
        this.translation = translation;
        this.adaptedToMatches = adaptedToMatches;
        this.glossariesMatches = glossaryMatches;
    }

    public String getText() {
        return text;
    }

    public String getTranslation() {
        return translation;
    }

    public List<NGMemoryMatch> getAdaptedToMatches() {
        return adaptedToMatches;
    }

    public List<NGGlossaryMatch> getGlossariesMatches() {
        return glossariesMatches;
    }
}
