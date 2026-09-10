package com.translated.lara.translator;

/** Options for importing a glossary file. */
public class GlossaryImportOptions {
    private Glossary.Type contentType = Glossary.Type.CSV_TABLE_UNI;
    private Boolean gzip = false;
    private String callbackUrl = null;

    public Glossary.Type getContentType() {
        return contentType;
    }

    public GlossaryImportOptions setContentType(Glossary.Type contentType) {
        this.contentType = contentType;
        return this;
    }

    /** Whether the supplied file is already gzip-compressed. Defaults to false; null also disables the marker. */
    public Boolean getGzip() {
        return gzip;
    }

    public GlossaryImportOptions setGzip(Boolean gzip) {
        this.gzip = gzip;
        return this;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public GlossaryImportOptions setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }
}
