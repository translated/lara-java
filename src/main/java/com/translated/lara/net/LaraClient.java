package com.translated.lara.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.Credentials;
import com.translated.lara.Version;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.json.DocumentStatusTypeAdapter;
import com.translated.lara.net.json.TextResultValueTypeAdapter;
import com.translated.lara.translator.Document;
import com.translated.lara.translator.TextResult;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LaraClient {

    private static final String SIGNING_ALGORITHM = "HmacSHA256";
    private static final SimpleDateFormat HTTP_DATE_FORMAT;

    static {
        HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(TextResult.Value.class, new TextResultValueTypeAdapter())
            .registerTypeAdapter(Document.Status.class, new DocumentStatusTypeAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private final String baseUrl;
    private final int connectionTimeout;
    private final int readTimeout;
    private final String accessKeyId;
    private final Key signingKey;
    private final Map<String, String> extraHeaders = new HashMap<>();

    public LaraClient(Credentials credentials) {
        this(credentials, new ClientOptions());
    }

    public LaraClient(Credentials credentials, ClientOptions options) {
        this.accessKeyId = credentials.getAccessKeyId();
        this.signingKey = new SecretKeySpec(credentials.getAccessKeySecret().getBytes(StandardCharsets.UTF_8), SIGNING_ALGORITHM);

        this.baseUrl = options.getServerUrl();
        this.connectionTimeout = (int) options.getConnectionTimeoutMs();
        this.readTimeout = (int) options.getReadTimeoutMs();
    }

    public void setExtraHeader(String name, String value) {
        extraHeaders.put(name, value);
    }

    public ClientResponse get(String path) throws LaraApiConnectionException {
        return get(path, null, null);
    }

    public ClientResponse get(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return get(path, params, null);
    }

    public ClientResponse get(String path, Map<String, Object> params, Map<String, String> headers) throws LaraApiConnectionException {
        return request("GET", path, params, null, headers);
    }

    public ClientResponse delete(String path) throws LaraException {
        return delete(path, null, null);
    }

    public ClientResponse delete(String path, Map<String, Object> params) throws LaraException {
        return delete(path, params, null);
    }

    public ClientResponse delete(String path, Map<String, Object> params, Map<String, String> headers) throws LaraApiConnectionException {
        return request("DELETE", path, params, null, headers);
    }

    public ClientResponse post(String path) throws LaraException {
        return post(path, null, null, null);
    }

    public ClientResponse post(String path, Map<String, Object> params) throws LaraException {
        return post(path, params, null, null);
    }

    public ClientResponse post(String path, Map<String, Object> params, Map<String, File> files) throws LaraApiConnectionException {
        return post(path, params, files, null);
    }

    public ClientResponse post(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraApiConnectionException {
        return request("POST", path, params, files, headers);
    }

    public ClientResponse put(String path) throws LaraException {
        return put(path, null, null, null);
    }

    public ClientResponse put(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return put(path, params, null, null);
    }

    public ClientResponse put(String path, Map<String, Object> params, Map<String, File> files) throws LaraApiConnectionException {
        return put(path, params, files, null);
    }

    public ClientResponse put(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraApiConnectionException {
        return request("PUT", path, params, files, headers);
    }

    public Stream<TextResult> postAndGetStream(String path, Map<String, Object> params) throws LaraException {
        return postAndGetStream(path, params, null, null);
    }
    public Stream<TextResult> postAndGetStream(String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        return postAndGetStream(path, params, null, headers);
    }
    public Stream<TextResult> postAndGetStream(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        return requestStream("POST", path, params, files, headers).map(this::parseTextResult);
    }

    private Stream<JsonElement> requestStream(String method, String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        path = normalizePath(path);
        params = prune(params);
        files = prune(files);

        RequestBody body = null;
        if (params != null || files != null) {
            if (files == null)
                body = new JsonRequestBody(gson, params);
            else
                body = new MultipartRequestBody(params, files);
        }

        HttpURLConnection connection = connect(baseUrl + path);

        try {
            // connection setup
            if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > 0) connection.setReadTimeout(readTimeout);
            connection.setUseCaches(false);

            // headers
            connection.setRequestProperty("X-HTTP-Method-Override", method);
            connection.setRequestProperty("Date", date());
            connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
            connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
            if (body != null) {
                String md5 = body.md5();
                if (md5 != null) connection.setRequestProperty("Content-MD5", md5);
                connection.setRequestProperty("Content-Type", body.contentType());
            }
            connection.setRequestProperty("Authorization", "Lara " + accessKeyId + ":" + sign(method, path, connection));

            // extra headers
            for (Map.Entry<String, String> header : extraHeaders.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());

            // headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet())
                    connection.setRequestProperty(header.getKey(), header.getValue());
            }

            // http method
            connection.setRequestMethod("POST");

            // body
            if (body != null) {
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    body.send(outputStream);
                }
            }

            connection.connect();

            int responseCode = connection.getResponseCode();

            boolean isSuccessful = responseCode >= 200 && responseCode < 300;
            if (!isSuccessful) {
                String errorBody = readErrorStream(connection);
                if (errorBody != null) {
                    throw new LaraApiConnectionException("HTTP error code: " + responseCode + ", body: " + errorBody);
                }
                throw new LaraApiConnectionException("HTTP error code: " + responseCode);
            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return reader.lines()
                .filter(line -> !line.trim().isEmpty())
                .map(line -> parseStreamLine(line, responseCode))
                .onClose(() -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // Ignore close errors
                    } finally {
                        connection.disconnect();
                    }
                });

        } catch (IOException e) {
            throw new LaraApiConnectionException("Streaming request failed: " + e.getMessage(), e);
        }
    }

    private ClientResponse request(String method, String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraApiConnectionException {
        path = normalizePath(path);
        params = prune(params);
        files = prune(files);

        RequestBody body = null;
        if (params != null || files != null) {
            if (files == null)
                body = new JsonRequestBody(gson, params);
            else
                body = new MultipartRequestBody(params, files);
        }

        HttpURLConnection connection = connect(baseUrl + path);

        try {
            // connection setup
            if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > 0) connection.setReadTimeout(readTimeout);
            connection.setUseCaches(false);

            // headers
            connection.setRequestProperty("X-HTTP-Method-Override", method);
            connection.setRequestProperty("Date", date());
            connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
            connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
            if (body != null) {
                String md5 = body.md5();
                if (md5 != null) connection.setRequestProperty("Content-MD5", md5);
                connection.setRequestProperty("Content-Type", body.contentType());
            }
            connection.setRequestProperty("Authorization", "Lara " + accessKeyId + ":" + sign(method, path, connection));

            // extra headers
            for (Map.Entry<String, String> header : extraHeaders.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());

            // headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet())
                    connection.setRequestProperty(header.getKey(), header.getValue());
            }

            // http method
            connection.setRequestMethod("POST");

            // body
            if (body != null) {
                connection.setDoOutput(true);
                try (OutputStream out = connection.getOutputStream()) {
                    body.send(out);
                }
            }

            return new ClientResponse(gson, connection);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to connect to URL: " + baseUrl + path, e);
        }
    }

    // utils

    private static HttpURLConnection connect(String url) throws LaraApiConnectionException {
        try {
            return (HttpURLConnection) new URL(url).openConnection();
        } catch (MalformedURLException e) {
            throw new LaraApiConnectionException("Invalid URL: " + url, e);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to open connection to URL: " + url, e);
        }
    }

    private static <T> Map<String, T> prune(Map<String, T> map) {
        if (map == null) return null;

        map.entrySet().removeIf(e -> e.getValue() == null);
        return map.isEmpty() ? null : map;
    }

    private static String normalizePath(String path) {
        return path.startsWith("/") ? path : ("/" + path);
    }

    private static String date() {
        return HTTP_DATE_FORMAT.format(new Date());
    }

    private static final Base64.Encoder BASE64 = Base64.getEncoder();

    private String sign(String method, String path, HttpURLConnection connection) {
        String date = header("Date", connection);
        String md5 = header("Content-MD5", connection);

        String contentType = header("Content-Type", connection);
        int separator = contentType.indexOf(';');
        if (separator > 0)
            contentType = contentType.substring(0, separator).trim();

        String challenge = method + "\n" + path + "\n" + md5 + "\n" + contentType + "\n" + date;

        Mac hmac;
        try {
            hmac = Mac.getInstance("HmacSHA256");
            hmac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("HmacSHA256 algorithm not found", e);
        } catch (InvalidKeyException e) {
            throw new Error("Invalid key", e);
        }

        byte[] signature = hmac.doFinal(challenge.getBytes(StandardCharsets.UTF_8));
        return BASE64.encodeToString(signature);
    }

    private static String header(String key, HttpURLConnection connection) {
        String value = connection.getRequestProperty(key);
        return value == null ? "" : value.trim();
    }

    private TextResult parseTextResult(JsonElement jsonElement) {
        try {
            return gson.fromJson(jsonElement, TextResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as TextResult", e);
        }
    }

    private JsonElement parseStreamLine(String line, int responseCode) {
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(line);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse streaming response line: " + line, e);
        }

        if (!jsonElement.isJsonObject()) {
            return jsonElement;
        }

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    int status = jsonObject.has("status") ? jsonObject.get("status").getAsInt() : responseCode;

    if (status < 200 || status >= 300) {
        throw new RuntimeException(buildStreamingApiException(status, jsonObject));
    }

    JsonElement data = jsonObject.has("data") ? jsonObject.get("data") : jsonElement;

        if (data.isJsonObject()) {
            JsonObject dataObject = data.getAsJsonObject();
            if (dataObject.has("content")) {
                return dataObject.get("content");
            }
        }

        return data;
    }

    private LaraApiException buildStreamingApiException(int status, JsonElement data) {
        String type = "UnknownError";
        String message = "An unknown error occurred";

        if (data != null && data.isJsonObject()) {
            JsonObject dataObject = data.getAsJsonObject();
            JsonObject errorObject = dataObject.has("error") && dataObject.get("error").isJsonObject()
                ? dataObject.getAsJsonObject("error")
                : dataObject;

            if (errorObject.has("type")) {
                type = errorObject.get("type").getAsString();
            }
            if (errorObject.has("message")) {
                message = errorObject.get("message").getAsString();
            }
        }

        return new LaraApiException(status, type, message);
    }

    // Helper method to read the error stream
    private String readErrorStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return null;
        }
    }

}
