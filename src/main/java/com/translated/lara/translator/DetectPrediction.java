package com.translated.lara.translator;

public class DetectPrediction {
    private final String language;
    private final double confidence;

    public DetectPrediction(String language, double confidence) {
        this.language = language;
        this.confidence = confidence;
    }

    public String getLanguage() {
        return language;
    }

    public double getConfidence() {
        return confidence;
    }
}