package com.translated.lara.errors;

public class LaraApiConnectionException extends LaraException {

    public LaraApiConnectionException(String message) {
        super(message);
    }

    public LaraApiConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
