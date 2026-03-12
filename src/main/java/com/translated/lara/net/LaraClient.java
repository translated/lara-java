package com.translated.lara.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.translated.lara.Version;
import com.translated.lara.authentication.AccessKey;
import com.translated.lara.authentication.AuthToken;
import com.translated.lara.authentication.AuthenticationResponse;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.json.AudioStatusTypeAdapter;
import com.translated.lara.net.json.DocumentStatusTypeAdapter;
import com.translated.lara.net.json.TextResultValueTypeAdapter;
import com.translated.lara.net.json.VoiceGenderTypeAdapter;
import com.translated.lara.translator.Audio;
import com.translated.lara.translator.Document;
import com.translated.lara.translator.TextResult;
import com.translated.lara.translator.VoiceGender;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.stream.Stream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LaraClient {

    private AuthToken authToken;
    private AccessKey accessKey;
    private final String baseUrl;
    private final Map<String, String> extraHeaders  = new HashMap<>();
    private final int connectionTimeout;
    private final int readTimeout;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(TextResult.Value.class, new TextResultValueTypeAdapter())
            .registerTypeAdapter(Document.Status.class, new DocumentStatusTypeAdapter())
            .registerTypeAdapter(Audio.Status.class, new AudioStatusTypeAdapter())
            .registerTypeAdapter(VoiceGender.class, new VoiceGenderTypeAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public LaraClient(AccessKey accessKey) {
        this(accessKey, new ClientOptions());
    }

    public LaraClient(AccessKey accessKey, ClientOptions options) {
        this.baseUrl = options.getServerUrl();
        this.connectionTimeout = (int) options.getConnectionTimeoutMs();
        this.readTimeout = (int) options.getReadTimeoutMs();
        this.accessKey = accessKey;
    }

    public LaraClient(AuthToken authToken, ClientOptions options) {
        this.baseUrl = options.getServerUrl();
        this.connectionTimeout = (int) options.getConnectionTimeoutMs();
        this.readTimeout = (int) options.getReadTimeoutMs();
        this.authToken = authToken;
    }

    public void setExtraHeader(String name, String value) {
        extraHeaders.put(name, value);
    }

    public ClientResponse get(String path) throws LaraException {
        return request("GET", path, null, null);
    }
    public ClientResponse get(String path, Map<String, String> headers) throws LaraException {
        return request("GET", path, null, headers);
    }

    public ClientResponse post(String path, Map<String, Object> params) throws LaraException {
        return request("POST", path, params, null);
    }
    public ClientResponse post(String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        return request("POST", path, params, headers);
    }
    public ClientResponse post(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        return request("POST", path, params, files, headers);
    }

    public ClientResponse put(String path, Map<String, Object> params) throws LaraException {
        return request("PUT", path, params, null);
    }
    public ClientResponse put(String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        return request("PUT", path, params, headers);
    }
    public ClientResponse put(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        return request("PUT", path, params, files, headers);
    }

    public ClientResponse delete(String path) throws LaraException {
        return request("DELETE", path, null, null);
    }
    public ClientResponse delete(String path, Map<String, Object> params) throws LaraException {
        return request("DELETE", path, params, null);
    }
    public ClientResponse delete(String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        return request("DELETE", path, params, headers);
    }

    public Stream<ClientResponse> postAndGetStream(String path, Map<String, Object> params) throws LaraException {
        return postAndGetStream(path, params, null, null);
    }
    public Stream<ClientResponse> postAndGetStream(String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        return postAndGetStream(path, params, null, headers);
    }
    public Stream<ClientResponse> postAndGetStream(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        return requestLineStream("POST", path, params, files, headers);
    }

    public InputStream postAndGetInputStream(String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        RequestBody body = buildRequestBody(params, files, true);
        return requestStream("POST", path, body, headers);
    }

    private Stream<ClientResponse> requestLineStream(String method, String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        RequestBody body = buildRequestBody(params, files, true);
        return requestLineStream(method, path, body, headers, false);
    }
    private Stream<ClientResponse> requestLineStream(String method, String path, RequestBody body, Map<String, String> headers, boolean isRetry) throws LaraException {
        path = normalizePath(path);
        headers = prune(headers);

        if (this.authToken == null || this.authToken.isTokenExpired()) {
            this.refreshOrReauthenticate();
        }

        HttpURLConnection connection = connect(baseUrl + path);

        try {
            if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > 0) connection.setReadTimeout(readTimeout);

            connection.setUseCaches(false);
            connection.setRequestProperty("Date", date());
            connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
            connection.setRequestProperty("Authorization", "Bearer " + authToken.getToken());

            if (Version.get() != null) {
                connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
            }

            // extra headers
            for (Map.Entry<String, String> header : extraHeaders.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());

            // headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet())
                    connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setRequestMethod(method);
            connection.setDoInput(true);

            if (body != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", body.contentType());

                try (OutputStream outputStream = connection.getOutputStream()) {
                    body.send(outputStream);
                }
            }

            connection.connect();

            int responseCode = connection.getResponseCode();

            boolean isSuccessful = responseCode >= 200 && responseCode < 300;
            if (!isSuccessful) {
                String errorBody = readErrorStream(connection);
                connection.disconnect();

                if (responseCode == 401 && !isRetry && errorBody != null && errorBody.contains("jwt expired")) {
                    this.refreshOrReauthenticate();
                    return requestLineStream(method, path, body, headers, true);
                }
                if (errorBody != null) {
                    throw new LaraApiConnectionException("HTTP error code: " + responseCode + ", body: " + errorBody);
                }
                throw new LaraApiConnectionException("HTTP error code: " + responseCode);
            }

            String contentType = connection.getContentType();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return reader.lines()
                .filter(line -> !line.trim().isEmpty())
                .map(line -> parseStreamLine(contentType, line, responseCode))
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
            connection.disconnect();
            throw new LaraApiConnectionException("Streaming request failed: " + e.getMessage(), e);
        }
    }

    private ClientResponse request(String method, String path, Map<String, Object> params, Map<String, String> headers) throws LaraException {
        RequestBody body = buildRequestBody(params, null, false);
        return request(method, path, body, headers, false);
    }
    private ClientResponse request(String method, String path, Map<String, Object> params, Map<String, File> files, Map<String, String> headers) throws LaraException {
        RequestBody body = buildRequestBody(params, files, false);
        return request(method, path, body, headers, false);
    }
    private ClientResponse request(String method, String path, RequestBody body, Map<String, String> headers, Boolean isRetry) throws LaraException {
        path = normalizePath(path);
        headers = prune(headers);

        if (this.authToken == null || this.authToken.isTokenExpired()) {
            this.refreshOrReauthenticate();
        }

        HttpURLConnection connection = connect(baseUrl + path);

        try {
            if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > 0) connection.setReadTimeout(readTimeout);

            connection.setUseCaches(false);
            connection.setRequestProperty("Date", date());
            connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
            connection.setRequestProperty("Authorization", "Bearer " + authToken.getToken());

            // extra headers
            for (Map.Entry<String, String> header : extraHeaders.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());

            // headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet())
                    connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setRequestMethod(method);

            // body
            if (body != null) {
                connection.setRequestProperty("Content-Type", body.contentType());
                connection.setDoOutput(true);
                try (OutputStream out = connection.getOutputStream()) {
                    body.send(out);
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 401) {
                String errorBody = readErrorStream(connection);

                if (errorBody != null && errorBody.contains("jwt expired") && !isRetry) {
                    this.refreshOrReauthenticate();
                    return this.request(method, path, body, headers, true);
                }
            }

            return ClientResponse.fromConnection(gson, connection);
        } catch(IOException e) {
            throw new LaraApiConnectionException("Failed to connect to URL: " + baseUrl );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private InputStream requestStream(String method, String path, RequestBody body, Map<String, String> headers) throws LaraException {
        return requestStream(method, path, body, headers, false);
    }
    private InputStream requestStream(String method, String path, RequestBody body, Map<String, String> headers, Boolean isRetry) throws LaraException {
        path = normalizePath(path);
        headers = prune(headers);

        if (this.authToken == null || this.authToken.isTokenExpired()) {
            this.refreshOrReauthenticate();
        }

        HttpURLConnection connection = connect(baseUrl + path);

        try {
            if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
            if (readTimeout > 0) connection.setReadTimeout(readTimeout);

            connection.setUseCaches(false);
            connection.setRequestProperty("Date", date());
            connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
            connection.setRequestProperty("Authorization", "Bearer " + authToken.getToken());

            if (Version.get() != null) {
                connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
            }

            // extra headers
            for (Map.Entry<String, String> header : extraHeaders.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());

            // headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet())
                    connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setRequestMethod(method);
            connection.setDoInput(true);

            if (body != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", body.contentType());

                try (OutputStream outputStream = connection.getOutputStream()) {
                    body.send(outputStream);
                }
            }

            connection.connect();

            int responseCode = connection.getResponseCode();

            boolean isSuccessful = responseCode >= 200 && responseCode < 300;
            if (!isSuccessful) {
                String errorBody = readErrorStream(connection);
                connection.disconnect();

                if (responseCode == 401 && !isRetry && errorBody != null && errorBody.contains("jwt expired")) {
                    this.refreshOrReauthenticate();
                    return requestStream(method, path, body, headers, true);
                }
                if (errorBody != null) {
                    throw new LaraApiConnectionException("HTTP error code: " + responseCode + ", body: " + errorBody);
                }
                throw new LaraApiConnectionException("HTTP error code: " + responseCode);
            }

            return connection.getInputStream();
        } catch (IOException e) {
            connection.disconnect();
            throw new LaraApiConnectionException("Streaming request failed: " + e.getMessage(), e);
        }
    }

    private void refreshOrReauthenticate() throws LaraException {
        if (this.authToken != null && this.authToken.getRefreshToken() != null
                && !this.authToken.getRefreshToken().isEmpty()) {
            try {
                this.refreshToken();
                return;
            } catch (LaraException e) {
                if (this.accessKey == null) throw e;
            }
        }

        if (this.accessKey != null) {
            this.authToken = this.authenticate(this.accessKey);
            return;
        }

        throw new LaraApiConnectionException("No authentication method available for token renewal");
    }

    private AuthToken authenticate(AccessKey accessKey) throws LaraException {
        String path = "/v2/auth";
        String method = "POST";
        String dateHeader = date();
        RequestBody body = new JsonRequestBody(gson, accessKey.toParams().build());

        HttpURLConnection connection = connect(baseUrl + path);
        String contentMd5 = body.md5();
        String contentType = body.contentType().split(";")[0].trim();

        connection.setUseCaches(false);
        connection.setRequestProperty("Date", dateHeader);
        connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
        connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
        if (connectionTimeout > 0) connection.setConnectTimeout(connectionTimeout);
        if (readTimeout > 0) connection.setReadTimeout(readTimeout);
        connection.setRequestProperty("Content-MD5", contentMd5);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Authorization", "Lara:" + sign(method, path, accessKey.getSecret(), connection));
        connection.setDoOutput(true);

        try {
            connection.setRequestMethod("POST");
            try (OutputStream out = connection.getOutputStream()){
                body.send(out);
            }

            ClientResponse response = ClientResponse.fromConnection(gson, connection);
            AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

            if (authResponse == null || authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new LaraApiConnectionException("Missing access token in authentication response: " + response);
            }

            String refreshToken = connection.getHeaderField("x-lara-refresh-token");
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new LaraApiConnectionException("Missing refresh token in authentication response");
            }
            return new AuthToken(authResponse.getToken(), refreshToken);

        } catch(IOException e) {
            throw new LaraApiConnectionException("Failed to connect to URL: " + baseUrl, e);
        } finally {
            connection.disconnect();
        }
    }

    private void refreshToken() throws LaraException {
        HttpURLConnection connection = connect(baseUrl + "/v2/auth/refresh");

        connection.setUseCaches(false);
        connection.setRequestProperty("Date", date());
        connection.setRequestProperty("X-Lara-SDK-Name", "lara-java");
        connection.setRequestProperty("X-Lara-SDK-Version", Version.get());
        connection.setRequestProperty("Authorization", "Bearer " + authToken.getRefreshToken());

        try {
            connection.setRequestMethod("POST");

            ClientResponse response = ClientResponse.fromConnection(gson, connection);
            AuthenticationResponse authResponse = response.as(AuthenticationResponse.class);

            if (authResponse == null || authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new LaraApiConnectionException("Missing access token in refresh response: " + response.toString());
            }

            String refreshToken = connection.getHeaderField("x-lara-refresh-token");
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new LaraApiConnectionException("Missing refresh token in refresh response");
            }
            this.authToken = new AuthToken(authResponse.getToken(), refreshToken);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to connect to URL: " + baseUrl, e);
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection connect(String url) throws LaraApiConnectionException{
        try {
            return (HttpURLConnection) new URL(url).openConnection();
        } catch (MalformedURLException e) {
            throw new LaraApiConnectionException("Invalid URL: " + url, e);
        } catch (IOException e) {
            throw new LaraApiConnectionException("Failed to open connection to URL: " + url, e);
        }
    }

    private String sign(String method, String path, String secret, HttpURLConnection connection) {
        String date = header("Date", connection);
        String md5 = header("Content-MD5", connection);

        String contentType = header("Content-Type", connection);
        int separator = contentType.indexOf(';');
        if (separator > 0)
            contentType = contentType.substring(0, separator).trim();

        String challenge = String.join("\n", method, path, md5, contentType, date);

        try {
            // Compute HMAC-SHA256
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKey);
            byte[] signatureBytes = hmacSha256.doFinal(challenge.getBytes(StandardCharsets.UTF_8));

            // Return Base64-encoded signature
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("HmacSHA256 algorithm not found", e);
        } catch (InvalidKeyException e) {
            throw new Error("Invalid key", e);
        }
    }

    private static String date() {
        SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        return HTTP_DATE_FORMAT.format(new Date());
    }

    private static <T> Map<String, T> prune(Map<String, T> map) {
        if (map == null) return null;

        map.entrySet().removeIf(e -> e.getValue() == null);
        return map.isEmpty() ? null : map;
    }

    private static String normalizePath(String path) {
        return path.startsWith("/") ? path : ("/" + path);
    }

    private static String header(String key, HttpURLConnection connection) {
        String value = connection.getRequestProperty(key);
        return value == null ? "" : value.trim();
    }

    // Helper method to read the error stream
    private String readErrorStream(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            // Likely no error stream available or it failed to read
            return null;
        }
    }

    private TextResult parseTextResult(JsonElement jsonElement) {
        try {
            return gson.fromJson(jsonElement, TextResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as TextResult", e);
        }
    }

    private ClientResponse parseStreamLine(String contentType, String line, int responseCode) {
        JsonObject root = JsonParser.parseString(line).getAsJsonObject();

        int httpStatus = root.has("status") ? root.get("status").getAsInt() : responseCode;
        JsonElement response = root.has("content")
                ? root.get("content")
                : root.has("error") ? root.get("error") : root;

        return new ClientResponse(gson, httpStatus, contentType, response);
    }

    private RequestBody buildRequestBody(Map<String, Object> params, Map<String, File> files, boolean includeEmptyBody) {
        params = prune(params);
        files = prune(files);

        if (files != null && !files.isEmpty()) {
            return new MultipartRequestBody(params, files);
        }
        if (params != null && !params.isEmpty()) {
            return new JsonRequestBody(gson, params);
        }
        if (includeEmptyBody) {
            return new JsonRequestBody(gson, new HashMap<>());
        }
        return null;
    }
}
