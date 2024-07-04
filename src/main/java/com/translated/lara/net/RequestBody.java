package com.translated.lara.net;

import java.io.IOException;
import java.io.OutputStream;

interface RequestBody {

    String md5();

    String contentType();

    void send(OutputStream body) throws IOException;

}
