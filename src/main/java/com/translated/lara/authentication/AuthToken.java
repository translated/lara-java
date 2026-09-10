package com.translated.lara.authentication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthToken {
    private final String token;
    private final String refreshToken;
    private final long expiresAtMs;

    public AuthToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = (refreshToken == null || refreshToken.isEmpty()) ? null : refreshToken;
        this.expiresAtMs = parseExpiresAtMs(token);
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

    public boolean isTokenExpired() {
        return expiresAtMs <= System.currentTimeMillis() + 5000L;
    }

    private static long parseExpiresAtMs(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid JWT format");

        JsonObject jsonObject;
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[1]);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
            jsonObject = JsonParser.parseString(decodedPayload).getAsJsonObject();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }

        if (!jsonObject.has("exp") || !jsonObject.get("exp").isJsonPrimitive())
            throw new IllegalArgumentException("JWT missing 'exp' claim");

        try {
            return jsonObject.get("exp").getAsLong() * 1000;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}
