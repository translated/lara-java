package com.translated.lara.errors;

public class S3Exception extends Exception {

    private final int statusCode;

    public S3Exception(int statusCode, String message) {
        super(message);

        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
