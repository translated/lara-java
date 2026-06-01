package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.HttpParams;
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

    public Styleguide create(String name, String content) throws LaraException {
        return client.post("/v2/styleguides", new HttpParams<>()
                .set("name", name)
                .set("content", content)
                .build()
        ).as(Styleguide.class);
    }

    /**
     * Updates the styleguide name only.
     */
    public Styleguide update(String id, String name) throws LaraException {
        return update(id, name, null);
    }

    /**
     * Updates a styleguide. Pass {@code null} for {@code name} or {@code content} to omit that field.
     */
    public Styleguide update(String id, String name, String content) throws LaraException {
        return client.put("/v2/styleguides/" + id, new HttpParams<>()
                .set("name", name)
                .set("content", content)
                .build()
        ).as(Styleguide.class);
    }

    public Styleguide delete(String id) throws LaraException {
        return client.delete("/v2/styleguides/" + id).as(Styleguide.class);
    }
}
