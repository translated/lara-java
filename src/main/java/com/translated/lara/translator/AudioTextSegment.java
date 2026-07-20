package com.translated.lara.translator;

/**
 * One translated transcript segment with source and translated text timings.
 */
public class AudioTextSegment {
    private Integer id;
    private Double start;
    private Double end;
    private String text;
    private String translation;

    public AudioTextSegment() {
    }

    public AudioTextSegment(Integer id, Double start, Double end, String text, String translation) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.text = text;
        this.translation = translation;
    }

    public Integer getId() {
        return id;
    }

    public Double getStart() {
        return start;
    }

    public Double getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public String getTranslation() {
        return translation;
    }
}
