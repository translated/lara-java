package com.translated.lara.errors;

public class LaraApiException extends LaraException {

    private final int statusCode;
    private final String type;

    public LaraApiException(int statusCode, String type, String message) {
        super(message);

        this.statusCode = statusCode;
        this.type = type;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

}
