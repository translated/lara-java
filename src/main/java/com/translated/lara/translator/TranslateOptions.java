package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslateOptions {

    public enum Priority {
        NORMAL, BACKGROUND
    }

    public enum UseCache {
        YES, NO, OVERWRITE
    }

    private String sourceHint = null;
    private String[] adaptTo = null;
    private String[] glossaries = null;
    private String[] instructions = null;
    private String contentType = null;
    private Boolean multiline = null;
    private Long timeoutMs = null;
    private Priority priority = null;
    private UseCache useCache = null;
    private Integer cacheTTL = null;
    private Boolean noTrace = null;
    private Boolean verbose = null;
    private TranslationStyle style = null;
    private Map<String, String> headers = null;

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

    public String[] getGlossaries() {
        return glossaries;
    }

    public TranslateOptions setGlossaries(List<String> glossaries) {
        this.glossaries = glossaries != null ? glossaries.toArray(new String[0]) : null;
        return this;
    }

    public TranslateOptions setGlossaries(String... glossaries) {
        this.glossaries = glossaries;
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

    public UseCache getUseCache() {
        return useCache;
    }

    public TranslateOptions setUseCache(UseCache useCache) {
        this.useCache = useCache;
        return this;
    }

    public Integer getCacheTTL() {
        return cacheTTL;
    }

    public TranslateOptions setCacheTTL(Integer cacheTTL) {
        this.cacheTTL = cacheTTL;
        return this;
    }

    public Boolean getNoTrace() {
        return noTrace;
    }

    public TranslateOptions setNoTrace(boolean noTrace) {
        this.noTrace = noTrace;
        return this;
    }

    public Boolean getVerbose() {
        return verbose;
    }

    public TranslateOptions setVerbose(Boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public TranslateOptions setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public TranslationStyle getStyle() {
        return style;
    }

    public TranslateOptions setStyle(TranslationStyle style) {
        this.style = style;
        return this;
    }

    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("source_hint", sourceHint);
        params.set("adapt_to", adaptTo);
        params.set("glossaries", glossaries);
        params.set("instructions", instructions);
        params.set("content_type", contentType);
        params.set("multiline", multiline);
        params.set("timeout", timeoutMs);
        params.set("priority", toString(priority));
        params.set("use_cache", toString(useCache));
        params.set("cache_ttl", cacheTTL);
        params.set("verbose", verbose);
        params.set("style", TranslationStyle.toString(style));

        return params;
    }

    private static String toString(Priority priority) {
        return priority != null ? priority.toString().toLowerCase() : null;
    }

    private static String toString(UseCache useCache) {
        return useCache != null ? useCache.toString().toLowerCase() : null;
    }

}
