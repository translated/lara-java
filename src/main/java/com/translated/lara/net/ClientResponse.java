package com.translated.lara.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraApiConnectionTimeoutException;
import com.translated.lara.errors.LaraApiException;
import com.translated.lara.translator.CollectionResult;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClientResponse {

    private final Gson gson;
    private final LaraApiException error;
    private final JsonElement data;
    private final String rawData;
    private final String contentType;
    private final Map<String, List<String>> headers;

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
                return new ClientResponse(gson, contentType, rawBody, connection.getHeaderFields());
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

            return new ClientResponse(gson, httpStatus, contentType, response,
                    connection.getHeaderFields());
        }
    }

    ClientResponse(Gson gson, int httpStatus, String contentType, JsonElement response) {
        this(gson, httpStatus, contentType, response, Collections.emptyMap());
    }

    ClientResponse(Gson gson, int httpStatus, String contentType, JsonElement response,
                   Map<String, List<String>> headers) {
        this.gson = gson;
        this.contentType = contentType;
        this.rawData = null;
        this.headers = copyHeaders(headers);

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
        this(gson, contentType, rawBody, Collections.emptyMap());
    }

    ClientResponse(Gson gson, String contentType, String rawBody, Map<String, List<String>> headers) {
        this.gson = gson;
        this.contentType = contentType;
        this.rawData = rawBody;
        this.data = null;
        this.error = null;
        this.headers = copyHeaders(headers);
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

    /** Decode a resource array and its pagination response headers. */
    public <T> CollectionResult<T> asCollection(Class<T> clazz)
            throws LaraApiException, LaraApiConnectionException {
        if (error != null) throw error;
        Long total = parseLong(getHeader("X-Total-Count"), 0, Long.MAX_VALUE);
        if (total == null)
            throw new LaraApiConnectionException("Missing or invalid X-Total-Count response header");
        Long currentPage = parseLong(getHeader("X-Page"), 1, Long.MAX_VALUE);
        Long size = parseLong(getHeader("X-Page-Size"), 1, Integer.MAX_VALUE);
        return new CollectionResult<>(asList(clazz), total,
                currentPage,
                size == null ? null : size.intValue());
    }

    private static Map<String, List<String>> copyHeaders(Map<String, List<String>> headers) {
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            copy.computeIfAbsent(entry.getKey().toLowerCase(Locale.ROOT), key -> new ArrayList<>())
                    .addAll(entry.getValue());
        }
        copy.replaceAll((key, values) -> Collections.unmodifiableList(values));
        return Collections.unmodifiableMap(copy);
    }

    private String getHeader(String name) {
        List<String> values = headers.get(name.toLowerCase(Locale.ROOT));
        return values == null || values.size() != 1 ? null : values.get(0);
    }

    private static Long parseLong(String value, long minimum, long maximum) {
        if (value == null || !value.matches("[0-9]+")) return null;
        try {
            long number = Long.parseLong(value);
            return number >= minimum && number <= maximum ? number : null;
        } catch (NumberFormatException e) {
            return null;
        }
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
