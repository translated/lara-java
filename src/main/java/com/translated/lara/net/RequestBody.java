package com.translated.lara.net;

import java.net.HttpURLConnection;

interface RequestBody {

    String md5();

    String contentType();

    void send(HttpURLConnection connection);

}
