package com.translated.lara.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

class JsonRequestBody implements RequestBody {

    private final byte[] body;

    public JsonRequestBody(Gson gson, Map<String, Object> params) {
        this.body = gson.toJson(params).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String md5() {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new Error("MD5 algorithm not found", e);
        }

        byte[] bytes = md5.digest(body);
        return HexFormat.format(bytes);
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public void send(OutputStream body) throws IOException {
        body.write(this.body);
    }

}
