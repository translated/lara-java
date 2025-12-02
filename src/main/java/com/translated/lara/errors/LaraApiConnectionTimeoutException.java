package com.translated.lara.errors;

public class LaraApiConnectionTimeoutException extends LaraApiConnectionException {
    public LaraApiConnectionTimeoutException(String message) {
        super(message);
    }

    public LaraApiConnectionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
