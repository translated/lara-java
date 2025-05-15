package com.translated.lara.translator;

public class Document {

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
    private final int translatedChars;
    private final int totalChars;
    private final String filename;
    private final String source;
    private final String target;
    private final DocumentOptions options;
    private final String errorReason;
    private final String createdAt;
    private final String updatedAt;

    public Document(String id, Status status, int translatedChars, int totalChars, String filename,
        String source, String target, DocumentOptions options, String error_reason, String createdAt, String updatedAt) {
        //
        this.id = id;
        this.status = status;
        this.translatedChars = translatedChars;
        this.totalChars = totalChars;
        this.filename = filename;
        this.source = source;
        this.target = target;
        this.options = options;
        this.errorReason = error_reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public int getTranslatedChars() {
        return translatedChars;
    }

    public int getTotalChars() {
        return totalChars;
    }

    public String getFilename() {
        return filename;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public DocumentOptions getOptions() {
        return options;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}