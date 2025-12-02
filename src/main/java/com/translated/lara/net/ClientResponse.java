package com.translated.lara.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraApiConnectionTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientResponse {

    private final Gson gson;
    private final LaraApiException error;
    private final JsonElement data;
    private final String rawData;
    private final String contentType;

    ClientResponse(Gson gson, HttpURLConnection connection) throws LaraApiConnectionException {
        this.gson = gson;

        int httpStatus;
        try {
            httpStatus = connection.getResponseCode();
        } catch (SocketTimeoutException e) {
            throw new LaraApiConnectionTimeoutException("Request timed out", e);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to get response code", e);
        }

        boolean isSuccessful = httpStatus >= 200 && httpStatus < 300;

        this.contentType = connection.getContentType();

        if (isSuccessful && contentType != null && contentType.contains("text/csv")) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                this.rawData = reader.lines().collect(Collectors.joining("\n"));
                this.data = null;
                this.error = null;
            } catch (IOException e) {
                throw new LaraApiConnectionException("Failed to get response stream", e);
            }
        } else {
            this.rawData = null;

            JsonElement response;
            try (Reader reader = new InputStreamReader(isSuccessful ? connection.getInputStream() : connection.getErrorStream(), StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                response = root.get(isSuccessful ? "content" : "error");
            } catch (IOException e) {
                throw new LaraApiConnectionException("Failed to get response stream", e);
            }

            if (isSuccessful) {
                this.error = null;
                this.data = response;
            } else {
                String type = "UnknownError";
                String message = "An unknown error occurred";

                if (response != null) {
                    JsonObject error = response.getAsJsonObject();
                    if (error.has("type"))
                        type = error.get("type").getAsString();
                    if (error.has("message"))
                        message = error.get("message").getAsString();
                }

                this.error = new LaraApiException(httpStatus, type, message);
                this.data = null;
            }
        }
    }

    public <T> T as(Class<T> clazz) throws LaraApiException {
        if (error != null) throw error;
        return gson.fromJson(data, clazz);
    }

    public <T> List<T> asList(Class<T> clazz) throws LaraApiException {
        if (error != null) throw error;

        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : data.getAsJsonArray())
            list.add(gson.fromJson(element, clazz));

        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        if (contentType != null && contentType.contains("text/csv")) {
            return rawData;
        } else {
            return gson.toJson(data);
        }
    }
}