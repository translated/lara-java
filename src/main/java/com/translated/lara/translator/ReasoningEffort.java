package com.translated.lara.translator;

/** Reasoning effort for text translation. */
public enum ReasoningEffort {
    LOW("low"), MEDIUM("medium"), HIGH("high");

    private final String value;

    ReasoningEffort(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ReasoningEffort effort) {
        return effort != null ? effort.toString() : null;
    }
}
