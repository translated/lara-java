package com.translated.lara.translator;

import java.util.List;

public class NGMemoryMatch {

    private final String memory;
    private final String tuid;
    private final List<String> language;
    private final String sentence;
    private final String translation;
    private final float score;

    public NGMemoryMatch(String memory, String tuid, List<String> language, String sentence, String translation, float score) {
        this.memory = memory;
        this.tuid = tuid;
        this.language=language;
        this.sentence = sentence;
        this.translation = translation;
        this.score = score;
    }

    public String getMemory() {
        return memory;
    }

    public String getTuid() {
        return tuid;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getSentence() {
        return sentence;
    }

    public String getTranslation() {
        return translation;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "NGMemoryMatch{" +
                "memory='" + memory + '\'' +
                ", tuid='" + tuid + '\'' +
                ", language=" + language +
                ", sentence='" + sentence + '\'' +
                ", translation='" + translation + '\'' +
                ", score=" + score +
                '}';
    }
}
