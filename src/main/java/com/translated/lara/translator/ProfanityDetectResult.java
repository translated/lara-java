package com.translated.lara.translator;

import java.util.List;

public class ProfanityDetectResult {
    private final String maskedText;
    private final List<DetectedProfanity> profanities;

    public ProfanityDetectResult(String maskedText, List<DetectedProfanity> profanities) {
        this.maskedText = maskedText;
        this.profanities = profanities;
    }

    public String getMaskedText() {
        return maskedText;
    }

    public List<DetectedProfanity> getProfanities() {
        return profanities;
    }

    @Override
    public String toString() {
        return "ProfanityDetectResult{maskedText='" + maskedText + "', profanities=" + profanities + "}";
    }
}
