package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

public class DocumentDownloadOptions {

    private String outputFormat = null;

    public String getOutputFormat() {
        return outputFormat;
    }

    public DocumentDownloadOptions setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("output_format", outputFormat);

        return params;
    }
}
