package com.translated.lara.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.translated.lara.Credentials;
import com.translated.lara.Version;
import com.translated.lara.errors.LaraApiConnectionException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.json.TextResultValueTypeAdapter;
import com.translated.lara.translator.TextResult;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
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

public class LaraClient {

    private static final String SIGNING_ALGORITHM = "HmacSHA256";
    private static final SimpleDateFormat HTTP_DATE_FORMAT;

    static {
        HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(TextResult.Value.class, new TextResultValueTypeAdapter())
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
        return get(path, null);
    }

    public ClientResponse get(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return request("GET", path, params, null);
    }

    public ClientResponse delete(String path) throws LaraException {
        return delete(path, null);
    }

    public ClientResponse delete(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return request("DELETE", path, params, null);
    }

    public ClientResponse post(String path) throws LaraException {
        return post(path, null, null);
    }

    public ClientResponse post(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return post(path, params, null);
    }

    public ClientResponse post(String path, Map<String, Object> params, Map<String, File> files) throws LaraApiConnectionException {
        return request("POST", path, params, files);
    }

    public ClientResponse put(String path) throws LaraException {
        return put(path, null, null);
    }

    public ClientResponse put(String path, Map<String, Object> params) throws LaraApiConnectionException {
        return put(path, params, null);
    }

    public ClientResponse put(String path, Map<String, Object> params, Map<String, File> files) throws LaraApiConnectionException {
        return request("PUT", path, params, files);
    }

    private ClientResponse request(String method, String path, Map<String, Object> params, Map<String, File> files) throws LaraApiConnectionException {
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

}
