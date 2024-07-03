package com.translated.lara;

public class Credentials {

    private final String accessKeyId;
    private final String accessKeySecret;

    public Credentials(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    @Override
    public String toString() {
        return accessKeyId;
    }

}
