package com.translated.lara.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class LaraJson {

    private static final Gson gson = new GsonBuilder().create();

    public static Gson get() {
        return gson;
    }
}
