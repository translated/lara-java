package com.translated.lara.translator;

import com.translated.lara.errors.LaraException;
import com.translated.lara.net.LaraClient;

import java.util.List;

public class Memories {

    private final LaraClient client;

    Memories(LaraClient client) {
        this.client = client;
    }

    public List<Memory> list() throws LaraException {
        return client.get("/memories").asList(Memory.class);
    }

}
