package com.translated.lara.net;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.Map;

class MultipartRequestBody implements RequestBody {
    public MultipartRequestBody(Map<String, Object> params, Map<String, File> files) {
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
