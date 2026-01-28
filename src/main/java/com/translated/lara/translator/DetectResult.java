package com.translated.lara.translator;

import java.util.List;

public class DetectResult {
    private final String language;
    private final String contentType;
    private final List<DetectPrediction> predictions;

    public DetectResult(String language, String contentType, List<DetectPrediction> predictions) {
        this.language = language;
        this.contentType = contentType;
        this.predictions = predictions;
    }

    public String getLanguage() {
        return language;
    }

    public String getContentType() {
        return contentType;
    }

    public List<DetectPrediction> getPredictions() {
        return predictions;
    }
}
