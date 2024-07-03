package com.translated.lara.errors;

public class LaraApiException extends LaraException {

    private final int httpCode;
    private final String type;
    private final String errorMessage;

    public LaraApiException(int httpCode, String name, String errorMessage) {
        super(String.format("[HTTP %d] %s: %s", httpCode, name, errorMessage));

        this.httpCode = httpCode;
        this.type = name;
        this.errorMessage = errorMessage;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getType() {
        return type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
