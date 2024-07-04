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
    private static final byte[] NLb = NL.getBytes(StandardCharsets.UTF_8);

    private final Map<String, String> params;
    private final Map<String, File> files;
    private final String boundary;

    private final byte[] boundaryBytes;

    public MultipartRequestBody(Map<String, Object> params, Map<String, File> files) {
        if (params != null && !params.isEmpty()) {
            this.params = new HashMap<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet())
                this.params.put(entry.getKey(), entry.getValue().toString());
        } else {
            this.params = null;
        }

        this.files = files != null && !files.isEmpty() ? files : null;
        this.boundary = "---------------------------LaraClient+" + UUID.randomUUID();
        this.boundaryBytes = ("--" + boundary + NL).getBytes(StandardCharsets.UTF_8);
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
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet())
                writeParam(body, entry.getKey(), entry.getValue());
        }

        if (files != null) {
            for (Map.Entry<String, File> entry : files.entrySet())
                writeFile(body, entry.getKey(), entry.getValue());
        }

        body.write(this.boundaryBytes);
        body.flush();
    }

    private void writeParam(OutputStream body, String key, String value) throws IOException {
        body.write(boundaryBytes);
        body.write(("Content-Disposition: form-data; name=\"" + key + "\"" + NL).getBytes(StandardCharsets.UTF_8));
        body.write(NLb);
        body.write(value.getBytes(StandardCharsets.UTF_8));
        body.write(NLb);
        body.flush();
    }

    private void writeFile(OutputStream body, String key, File value) throws IOException {
        String filename = value.getName();
        String contentType = URLConnection.guessContentTypeFromName(filename);

        body.write(boundaryBytes);
        body.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + filename + "\"" + NL).getBytes(StandardCharsets.UTF_8));
        body.write(("Content-Type: " + contentType + NL).getBytes(StandardCharsets.UTF_8));
        body.write(("Content-Transfer-Encoding: binary" + NL).getBytes(StandardCharsets.UTF_8));
        body.write(NLb);

        try (InputStream inStream = Files.newInputStream(value.toPath())) {
            byte[] buffer = new byte[4096];

            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1)
                body.write(buffer, 0, bytesRead);
        }

        body.write(NLb);
        body.flush();
    }

}
