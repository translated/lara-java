package com.translated.lara.translator;

public enum ProfanityFilter {

    DETECT("detect"),
    AVOID("avoid"),
    HIDE("hide");

    private final String value;

    ProfanityFilter(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ProfanityFilter filter) {
        return filter != null ? filter.toString() : null;
    }
}
