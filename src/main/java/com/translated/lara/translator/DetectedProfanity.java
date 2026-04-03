package com.translated.lara.translator;

public class DetectedProfanity {
    private final String text;
    private final int startCharIndex;
    private final int endCharIndex;
    private final double score;

    public DetectedProfanity(String text, int startCharIndex, int endCharIndex, double score) {
        this.text = text;
        this.startCharIndex = startCharIndex;
        this.endCharIndex = endCharIndex;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public int getStartCharIndex() {
        return startCharIndex;
    }

    public int getEndCharIndex() {
        return endCharIndex;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "DetectedProfanity{text='" + text + "', startCharIndex=" + startCharIndex +
                ", endCharIndex=" + endCharIndex + ", score=" + score + "}";
    }
}
