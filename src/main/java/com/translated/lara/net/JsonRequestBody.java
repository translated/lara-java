package com.translated.lara.net;

import java.net.HttpURLConnection;
import java.util.Map;

class JsonRequestBody implements RequestBody {

    public JsonRequestBody(Map<String, Object> params) {
    }

    @Override
    public String md5() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String contentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send(HttpURLConnection connection) {
        throw new UnsupportedOperationException();
    }

}
