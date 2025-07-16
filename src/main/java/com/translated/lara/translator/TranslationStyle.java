package com.translated.lara.translator;

/**
 * Translation style constants
 */
public enum TranslationStyle {
    
    FAITHFUL("faithful"),
    FLUID("fluid"),
    CREATIVE("creative");
    
    private final String value;
    
    TranslationStyle(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    public static String toString(TranslationStyle style) {
        return style != null ? style.toString() : null;
    }

} 