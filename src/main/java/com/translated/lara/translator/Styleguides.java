package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.util.List;
import java.util.Map;

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

    public StyleguideShares getShares(String id) throws LaraException {
        return client.get("/v2/styleguides/" + id + "/shares").as(StyleguideShares.class);
    }

    public Styleguide addAccountShare(String id) throws LaraException { return addAccountShare(id, null); }

    public Styleguide addAccountShare(String id, String name) throws LaraException {
        return client.post("/v2/styleguides/" + id + "/shares", shareName(name)).as(Styleguide.class);
    }

    public Styleguide renameAccountShare(String id, String name) throws LaraException {
        return client.put("/v2/styleguides/" + id + "/shares", shareName(name)).as(Styleguide.class);
    }

    public Styleguide revokeAccountShare(String id) throws LaraException {
        return client.delete("/v2/styleguides/" + id + "/shares").as(Styleguide.class);
    }

    public Styleguide addGroupShare(String id, String groupId) throws LaraException { return addGroupShare(id, groupId, null); }

    public Styleguide addGroupShare(String id, String groupId, String name) throws LaraException {
        return client.post("/v2/styleguides/" + id + "/shares/groups/" + groupId, shareName(name)).as(Styleguide.class);
    }

    public Styleguide renameGroupShare(String id, String groupId, String name) throws LaraException {
        return client.put("/v2/styleguides/" + id + "/shares/groups/" + groupId, shareName(name)).as(Styleguide.class);
    }

    public Styleguide revokeGroupShare(String id, String groupId) throws LaraException {
        return client.delete("/v2/styleguides/" + id + "/shares/groups/" + groupId).as(Styleguide.class);
    }

    private Map<String, Object> shareName(String name) {
        return new HttpParams<>().set("name", name).build();
    }
}
