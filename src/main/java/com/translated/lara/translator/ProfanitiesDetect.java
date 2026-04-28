package com.translated.lara.translator;

public enum ProfanitiesDetect {

    TARGET("target"),
    SOURCE_TARGET("source_target");

    private final String value;

    ProfanitiesDetect(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ProfanitiesDetect detect) {
        return detect != null ? detect.toString() : null;
    }
}
