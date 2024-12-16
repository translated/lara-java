package com.translated.lara.net;

public class ClientOptions {

    private static final String DEFAULT_SERVER_URL = "https://api.laratranslate.com";

    private String serverUrl = null;
    private long connectionTimeoutMs = 0;
    private long readTimeoutMs = 0;

    public String getServerUrl() {
        return serverUrl == null ? DEFAULT_SERVER_URL : serverUrl;
    }

    public ClientOptions setServerUrl(String serverUrl) {
        if (serverUrl != null) {
            while (serverUrl.endsWith("/")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
            }
        }

        this.serverUrl = serverUrl;
        return this;
    }

    public long getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public ClientOptions setConnectionTimeoutMs(long connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
        return this;
    }

    public long getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public ClientOptions setReadTimeoutMs(long readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
        return this;
    }

}
