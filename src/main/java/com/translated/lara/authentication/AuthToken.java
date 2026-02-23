package com.translated.lara.authentication;

public class AuthToken {
    private final String token;
    private final String refreshToken;

    public AuthToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return token;
    }
}
