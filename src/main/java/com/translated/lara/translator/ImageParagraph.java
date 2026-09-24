package com.translated.lara.translator;

import java.util.List;

/**
 * Translated image text with optional memory and glossary matches.
 * Responses with layout metadata use {@link ImageLayoutParagraph}.
 */
public class ImageParagraph {

    private final String text;
    private final String translation;
    private final List<NGMemoryMatch> adaptedToMatches;
    private final List<NGGlossaryMatch> glossariesMatches;

    /** Creates a text-only paragraph suitable for generative rendering. */
    public ImageParagraph(String text, String translation) {
        this(text, translation, null, null);
    }

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
