package com.translated.lara.translator;

import java.util.List;

public class NGGlossaryMatch {
    private final String glossary;
    private final List<String> language;
    private final String term;
    private final String translation;

    public NGGlossaryMatch(String glossary, List<String> language, String term, String translation) {
        this.glossary = glossary;
        this.language = language;
        this.term = term;
        this.translation = translation;
    }

    public String getGlossary() {
        return glossary;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getTerm() {
        return term;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public String toString() {
        return "NGGlossaryMatch{" +
                "glossary='" + glossary + '\'' +
                ", language=" + language +
                ", term='" + term + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
