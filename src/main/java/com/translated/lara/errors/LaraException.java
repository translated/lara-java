package com.translated.lara.errors;

public class LaraException extends Exception {

    protected LaraException(String message) {
        super(message);
    }

    protected LaraException(String message, Throwable cause) {
        super(message, cause);
    }

}
