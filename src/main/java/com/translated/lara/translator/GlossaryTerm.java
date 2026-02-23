package com.translated.lara.translator;

public class GlossaryTerm {
    private String language;
    private String value;

    public GlossaryTerm(String language, String value) {
        this.language = language;
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GlossaryTerm{" +
                "language='" + language + '\'' +
                ", value='" + value + '\'' +
                '}';
        }
}
