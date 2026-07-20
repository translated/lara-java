package com.translated.lara.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Structured translated transcript returned by the audio transcript flow.
 */
public class AudioTextResult {
    private String id;
    private String source;
    private String target;
    private String filename;
    private Double duration;
    private String text;
    private String translation;
    private List<AudioTextSegment> segments = new ArrayList<>();

    public AudioTextResult() {
    }

    public AudioTextResult(String id, String source, String target, String filename, Double duration,
                           String text, String translation, List<AudioTextSegment> segments) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.filename = filename;
        this.duration = duration;
        this.text = text;
        this.translation = translation;
        this.segments = segments != null ? new ArrayList<>(segments) : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getFilename() {
        return filename;
    }

    public Double getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }

    public String getTranslation() {
        return translation;
    }

    public List<AudioTextSegment> getSegments() {
        return segments == null ? Collections.emptyList() : Collections.unmodifiableList(segments);
    }
}
