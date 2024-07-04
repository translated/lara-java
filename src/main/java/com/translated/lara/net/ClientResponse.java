package com.translated.lara.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraApiException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientResponse {

    private final Gson gson;
    private final LaraApiException error;
    private final JsonElement data;

    ClientResponse(Gson gson, HttpURLConnection connection) throws LaraApiConnectionException {
        this.gson = gson;

        int httpStatus;
        try {
            httpStatus = connection.getResponseCode();
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to get response code", e);
        }

        boolean isSuccessful = httpStatus >= 200 && httpStatus < 300;

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
            String name = "UnknownError";
            String message = "An unknown error occurred";

            if (response != null) {
                JsonObject error = response.getAsJsonObject();
                if (error.has("type"))
                    name = error.get("type").getAsString();
                if (error.has("message"))
                    message = error.get("message").getAsString();
            }

            this.error = new LaraApiException(httpStatus, name, message);
            this.data = null;
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

}
