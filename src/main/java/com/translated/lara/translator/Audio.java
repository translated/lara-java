package com.translated.lara.translator;

/**
 * Represents an audio translation job handled by the LARA API.
 * <p>
 * Instances of this class are typically returned by {@link AudioTranslator}
 * when creating or querying an audio translation request.
 */
public class Audio {

    /**
     * Enumeration of the possible states of an audio translation job.
     */
    public enum Status {
        INITIALIZED,
        ANALYZING,
        PAUSED,
        READY,
        TRANSLATING,
        TRANSLATED,
        ERROR
    }

    private final String id;
    private final Status status;
    private final int translatedSeconds;
    private final int totalSeconds;
    private final String filename;
    private final String source;
    private final String target;
    private final AudioOptions options;
    private final String errorReason;
    private final String createdAt;
    private final String updatedAt;

    /**
     * Creates a new {@code Audio} instance.
     *
     * @param id                unique identifier of the audio resource
     * @param status            current {@link Status} of the translation job
     * @param translatedSeconds number of seconds that have already been translated
     * @param totalSeconds      total duration of the audio in seconds
     * @param filename          original file name of the uploaded audio
     * @param source            ISO language code of the source language
     * @param target            ISO language code of the target language
     * @param options           options used for the audio translation, if any
     * @param error_reason      error description returned by the API when {@code status == Status.ERROR}
     * @param createdAt         creation timestamp of the audio resource
     * @param updatedAt         last update timestamp of the audio resource
     */
    public Audio(String id, Status status, int translatedSeconds, int totalSeconds, String filename, String source,
                 String target, AudioOptions options, String error_reason, String createdAt, String updatedAt) {
        this.id = id;
        this.status = status;
        this.translatedSeconds = translatedSeconds;
        this.totalSeconds = totalSeconds;
        this.filename = filename;
        this.source = source;
        this.target = target;
        this.options = options;
        this.errorReason = error_reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the unique identifier of the audio resource.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the current {@link Status} of the translation job.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the number of seconds that have already been translated.
     */
    public int getTranslatedSeconds() {
        return translatedSeconds;
    }

    /**
     * Returns the total duration of the audio in seconds.
     */
    public int getTotalSeconds() {
        return totalSeconds;
    }

    /**
     * Returns the original file name of the uploaded audio.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the ISO language code of the source language.
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the ISO language code of the target language.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Returns the options used for the audio translation, if any.
     */
    public AudioOptions getOptions() {
        return options;
    }

    /**
     * Returns the error description when the job status is {@link Status#ERROR}.
     */
    public String getErrorReason() {
        return errorReason;
    }

    /**
     * Returns the creation timestamp of the audio resource.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the last update timestamp of the audio resource.
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
}
