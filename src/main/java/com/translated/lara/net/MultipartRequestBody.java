package com.translated.lara.net;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class MultipartRequestBody implements RequestBody {

    private static final String NL = "\r\n";

    private final Map<String, String> params;
    private final Map<String, File> files;
    private final String boundary;

    public MultipartRequestBody(Map<String, Object> params, Map<String, File> files) {
        if (params != null && !params.isEmpty()) {
            this.params = new HashMap<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet())
                this.params.put(entry.getKey(), entry.getValue().toString());
        } else {
            this.params = null;
        }

        this.files = files != null && !files.isEmpty() ? files : null;
        this.boundary = "---------------------------LaraClient_" + UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String md5() {
        if (params == null) return null;

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new Error("MD5 algorithm not found", e);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            md5.update(entry.getKey().getBytes(StandardCharsets.UTF_8));
            md5.update(entry.getValue().getBytes(StandardCharsets.UTF_8));
        }

        return HexFormat.format(md5.digest());
    }

    @Override
    public String contentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public void send(OutputStream body) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(body, StandardCharsets.UTF_8));

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                writer.append("--").append(boundary).append(NL);
                writer.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(NL);
                writer.append(NL);
                writer.append(value);
                writer.append(NL);
            }
        }

        writer.flush();

        if (files != null) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String key = entry.getKey();
                File value = entry.getValue();

                String filename = value.getName();
                String contentType = URLConnection.guessContentTypeFromName(filename);

                writer.append("--").append(boundary).append(NL);
                writer.append("Content-Disposition: form-data; name=\"").append(key).append("\"; filename=\"").append(filename).append("\"").append(NL);
                writer.append("Content-Type: ").append(contentType).append(NL);
                writer.append("Content-Transfer-Encoding: binary").append(NL);
                writer.append(NL);
                writer.flush();

                try (InputStream inStream = Files.newInputStream(value.toPath())) {
                    byte[] buffer = new byte[4096];

                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1)
                        body.write(buffer, 0, bytesRead);
                }
                body.flush();

                writer.append(NL);
                writer.flush();
            }
        }

        writer.append("--").append(boundary).append("--").append(NL);
        writer.flush();
        writer.close();
    }

}
