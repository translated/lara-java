package com.translated.lara.translator;

import com.google.gson.annotations.SerializedName;

public class IntelligenceError {
    @SerializedName(value = "statusCode", alternate = {"status_code"})
    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
