package com.translated.lara.authentication;

import com.translated.lara.net.HttpParams;

public class AccessKey {
    protected final String id;
    protected final String secret;

    public AccessKey(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("id", id);

        return params;
    }

    @Override
    public String toString() {
        return id;
    }
}
