package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

import java.util.List;

public class DocumentTranslateOptions {

    private String[] adaptTo = null;
    private String outputFormat = null;
    private Boolean noTrace = null;

    public String[] getAdaptTo() {
        return adaptTo;
    }

    public DocumentTranslateOptions setAdaptTo(List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    public DocumentTranslateOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public DocumentTranslateOptions setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public Boolean getNoTrace() {
        return noTrace;
    }

    public DocumentTranslateOptions setNoTrace(Boolean noTrace) {
        this.noTrace = noTrace;
        return this;
    }

    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("adapt_to", adaptTo);
        params.set("output_format", outputFormat);

        return params;
    }
}
