package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

import java.util.List;

public class DocumentUploadOptions {

    private String[] adaptTo = null;
    private Boolean noTrace = null;

    public String[] getAdaptTo() {
        return adaptTo;
    }

    public DocumentUploadOptions setAdaptTo(List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    public DocumentUploadOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }

    public Boolean getNoTrace() {
        return noTrace;
    }

    public DocumentUploadOptions setNoTrace(Boolean noTrace) {
        this.noTrace = noTrace;
        return this;
    }

    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("adapt_to", adaptTo);

        return params;
    }
}
