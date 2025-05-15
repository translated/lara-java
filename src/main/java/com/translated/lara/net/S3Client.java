package com.translated.lara.net;

import com.translated.lara.errors.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class S3Client {

    public void upload(String url, Map<String, Object> s3Params, File file) throws S3Exception {
        RequestBody body = new MultipartRequestBody(s3Params, Collections.singletonMap("file", file));
        HttpURLConnection connection = null;

        try {
            // Initialize the connection
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", body.contentType());

            // Send the multipart request body
            body.send(connection.getOutputStream());

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
                String errMsg = readErrorBody(connection);

                throw new S3Exception(responseCode, errMsg);
            }
        } catch (IOException e) {
            throw new S3Exception(500, e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public InputStream download(String url) throws S3Exception {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new S3Exception(500, e.getMessage());
        }
    }

    private static String readErrorBody(HttpURLConnection conn) {
        InputStream is = conn.getErrorStream();
        if (is == null) {
            return "";
        }
        try (InputStream in = is; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) != -1) {
                baos.write(buf, 0, n);
            }
            return baos.toString(StandardCharsets.UTF_8.name()).trim();
        } catch (IOException ignored) {
            return "";
        }
    }

}
