package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.LaraClient;

import java.util.List;

public class Styleguides {
    private final LaraClient client;

    Styleguides(LaraClient client) {
        this.client = client;
    }

    public List<Styleguide> list() throws LaraException {
        return client.get("/v2/styleguides").asList(Styleguide.class);
    }

    public Styleguide get(String id) throws LaraException {
        try {
            return client.get("/v2/styleguides/" + id).as(Styleguide.class);
        } catch (LaraApiException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }

            throw e;
        }
    }
}
