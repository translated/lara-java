package com.translated.lara.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraApiConnectionTimeoutException;
import com.translated.lara.errors.LaraApiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientResponse {

    private final Gson gson;
    private final LaraApiException error;
    private final JsonElement data;
    private final String rawData;
    private final String contentType;

    static ClientResponse fromConnection(Gson gson, HttpURLConnection connection) throws LaraApiConnectionException {
        String contentType = connection.getContentType();

        int httpStatus;
        try {
            httpStatus = connection.getResponseCode();
        } catch (SocketTimeoutException e) {
            throw new LaraApiConnectionTimeoutException("Request timed out", e);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to get response code", e);
        }

        boolean isSuccessful = httpStatus >= 200 && httpStatus < 300;
        if (isSuccessful && isRawTextContent(contentType)) {
            try (InputStream input = connection.getInputStream();
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                String rawBody = new String(output.toByteArray(), StandardCharsets.UTF_8);
                return new ClientResponse(gson, contentType, rawBody);
            } catch (IOException e) {
                throw new LaraApiConnectionException("Failed to get response stream", e);
            }
        } else {
            JsonElement response;
            try (Reader reader = new InputStreamReader(isSuccessful ? connection.getInputStream() : connection.getErrorStream(), StandardCharsets.UTF_8)) {
                response = JsonParser.parseReader(reader);
            } catch (IOException e) {
                throw new LaraApiConnectionException("Failed to get response stream", e);
            }

            return new ClientResponse(gson, httpStatus, contentType, response);
        }
    }

    ClientResponse(Gson gson, int httpStatus, String contentType, JsonElement response) {
        this.gson = gson;
        this.contentType = contentType;
        this.rawData = null;

        if (httpStatus >= 200 && httpStatus < 300) {
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

    ClientResponse(Gson gson, String contentType, String rawBody) {
        this.gson = gson;
        this.contentType = contentType;
        this.rawData = rawBody;
        this.data = null;
        this.error = null;
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
        if (isRawTextContent(contentType)) {
            return rawData;
        } else {
            return gson.toJson(data);
        }
    }

    private static boolean isRawTextContent(String contentType) {
        return contentType != null
                && (contentType.contains("text/csv") || contentType.contains("application/xml"));
    }
}
