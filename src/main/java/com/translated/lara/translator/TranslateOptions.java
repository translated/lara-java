package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

import java.util.List;

public class TranslateOptions {

    public enum Priority {
        NORMAL, BACKGROUND
    }

    private String sourceHint = null;
    private String[] adaptTo = null;
    private String[] instructions = null;
    private String contentType = null;
    private Boolean multiline = null;
    private Long timeoutMs = null;
    private Priority priority = null;

    static HttpParams<Object> toParams(TranslateOptions options) {
        HttpParams<Object> params = new HttpParams<>();
        if (options != null) {
            params.set("source_hint", options.sourceHint);
            params.set("adapt_to", options.adaptTo);
            params.set("instructions", options.instructions);
            params.set("content_type", options.contentType);
            params.set("multiline", options.multiline);
            params.set("timeout", options.timeoutMs);
            params.set("priority", options.priority);
        }

        return params;
    }

    public String getSourceHint() {
        return sourceHint;
    }

    public TranslateOptions setSourceHint(String sourceHint) {
        this.sourceHint = sourceHint;
        return this;
    }

    public String[] getAdaptTo() {
        return adaptTo;
    }

    public TranslateOptions setAdaptTo(List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    public TranslateOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public TranslateOptions setInstructions(List<String> instructions) {
        this.instructions = instructions != null ? instructions.toArray(new String[0]) : null;
        return this;
    }

    public TranslateOptions setInstructions(String... instructions) {
        this.instructions = instructions;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public TranslateOptions setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Boolean getMultiline() {
        return multiline;
    }

    public TranslateOptions setMultiline(boolean multiline) {
        this.multiline = multiline;
        return this;
    }

    public Long getTimeoutMs() {
        return timeoutMs;
    }

    public TranslateOptions setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public TranslateOptions setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

}
