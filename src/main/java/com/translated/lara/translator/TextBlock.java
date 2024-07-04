package com.translated.lara.translator;

public class TextBlock {

    private final String text;
    private final boolean translatable;

    public TextBlock(String text) {
        this(text, true);
    }

    public TextBlock(String text, boolean translatable) {
        this.text = text;
        this.translatable = translatable;
    }

    public String getText() {
        return text;
    }

    public boolean isTranslatable() {
        return translatable;
    }

    @Override
    public String toString() {
        return "{" + translatable + ", " + text + "}";
    }

}
