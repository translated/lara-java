package com.translated.lara.translator;

import java.util.Map;

public class S3UploadParams {
    private String url;
    private Map<String, Object> fields;

    public S3UploadParams(String url, Map<String, Object> fields) {
        this.url = url;
        this.fields = fields;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public String getS3Key() {
        return (String) fields.get("key");
    }
}