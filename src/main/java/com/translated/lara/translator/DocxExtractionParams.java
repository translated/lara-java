package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

/**
 * Extraction parameters for DOCX files.
 */
public class DocxExtractionParams extends DocumentExtractionParams {

    private Boolean extractComments = null;
    private Boolean acceptRevisions = null;

    public Boolean getExtractComments() {
        return extractComments;
    }

    public DocxExtractionParams setExtractComments(Boolean extractComments) {
        this.extractComments = extractComments;
        return this;
    }

    public Boolean getAcceptRevisions() {
        return acceptRevisions;
    }

    public DocxExtractionParams setAcceptRevisions(Boolean acceptRevisions) {
        this.acceptRevisions = acceptRevisions;
        return this;
    }

    @Override
    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        if (extractComments != null) {
            params.set("extract_comments", extractComments);
        }
        if (acceptRevisions != null) {
            params.set("accept_revisions", acceptRevisions);
        }
        return params;
    }
}
