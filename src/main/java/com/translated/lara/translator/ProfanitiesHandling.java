package com.translated.lara.translator;

public enum ProfanitiesHandling {

    DETECT("detect"),
    AVOID("avoid"),
    HIDE("hide");

    private final String value;

    ProfanitiesHandling(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ProfanitiesHandling handling) {
        return handling != null ? handling.toString() : null;
    }
}
