package com.translated.lara.translator;

import java.util.List;

public class ProfanityDetectResult {
    private final String maskedText;
    private final List<DetectedProfanity> profanities;
    private final String error;

    public ProfanityDetectResult(String maskedText, List<DetectedProfanity> profanities, String error) {
        this.maskedText = maskedText;
        this.profanities = profanities;
        this.error = error;
    }

    public String getMaskedText() {
        return maskedText;
    }

    public List<DetectedProfanity> getProfanities() {
        return profanities;
    }

    public String getError() { return error; }

    @Override
    public String toString() {
        return "ProfanityDetectResult{maskedText='" + maskedText + "', profanities=" + profanities + "', error=" + error + "}}";
    }
}
