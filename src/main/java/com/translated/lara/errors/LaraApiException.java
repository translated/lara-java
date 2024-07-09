package com.translated.lara.errors;

public class LaraApiException extends LaraException {

    private final int statusCode;
    private final String type;
    private final String errorMessage;

    public LaraApiException(int statusCode, String name, String errorMessage) {
        super(String.format("[HTTP %d] %s: %s", statusCode, name, errorMessage));

        this.statusCode = statusCode;
        this.type = name;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
