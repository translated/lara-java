package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.TimeoutException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Glossaries {
    private final LaraClient client;
    private final long pollingInterval;

    Glossaries(LaraClient client) {
        this(client, 2000);
    }

    Glossaries(LaraClient client, long pollingInterval) {
        this.client = client;
        this.pollingInterval = pollingInterval;
    }

    public List<Glossary> list() throws LaraException {
        return client.get("/glossaries").asList(Glossary.class);
    }

    public Glossary create(String name) throws LaraException {
        return client.post("/glossaries", new HttpParams<>()
                .set("name", name)
                .build()
        ).as(Glossary.class);
    }

    public Glossary get(String id) throws LaraException {
        try {
            return client.get("/glossaries/" + id).as(Glossary.class);
        } catch (LaraApiException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }

            throw e;
        }
    }

    public Glossary delete(String id) throws LaraException {
        return client.delete("/glossaries/" + id).as(Glossary.class);
    }

    public Glossary update(String id, String name) throws LaraException {
        return client.put("/glossaries/" + id, new HttpParams<>()
                .set("name", name)
                .build()
        ).as(Glossary.class);
    }

    public GlossaryImport importCsv(String id, File csv) throws LaraException {
        return importCsv(id, csv, csv.getName().toLowerCase().endsWith(".gz"));
    }

    public GlossaryImport importCsv(String id, File csv, boolean gzip) throws LaraException {
        Map<String, Object> params = new HttpParams<>()
                .set("compression", gzip ? "gzip" : null)
                .build();
        Map<String, File> files = new HttpParams<File>()
                .set("csv", csv)
                .build();

        return client.post("/glossaries/" + id + "/import", params, files).as(GlossaryImport.class);
    }

    public GlossaryImport getImportStatus(String id) throws LaraException {
        return client.get("/glossaries/imports/" + id).as(GlossaryImport.class);
    }

    public GlossaryImport waitForImport(GlossaryImport gImport) throws LaraException, InterruptedException {
        return waitForImport(gImport, null, 0);
    }

    public GlossaryImport waitForImport(GlossaryImport gImport, Consumer<GlossaryImport> updateCallback) throws LaraException, InterruptedException {
        return waitForImport(gImport, updateCallback, 0);
    }

    public GlossaryImport waitForImport(GlossaryImport gImport, long maxWaitTimeMs) throws LaraException, InterruptedException {
        return waitForImport(gImport, null, maxWaitTimeMs);
    }

    public GlossaryImport waitForImport(GlossaryImport gImport, Consumer<GlossaryImport> updateCallback, long maxWaitTimeMs) throws LaraException, InterruptedException {
        long start = System.currentTimeMillis();
        while (gImport.getProgress() < 1.) {
            if (maxWaitTimeMs > 0 && System.currentTimeMillis() - start > maxWaitTimeMs)
                throw new TimeoutException();

            Thread.sleep(this.pollingInterval);

            gImport = getImportStatus(gImport.getId());
            if (updateCallback != null)
                updateCallback.accept(gImport);
        }

        return gImport;
    }

    public String export(String id, Glossary.Type contentType, String source) throws LaraException {
        HttpParams<Object> params = new HttpParams<>()
                .set("content_type", contentType.toString());
        if (source != null) {
            params.set("source", source);
        }
        return client.get("/glossaries/" + id + "/export", params.build()).toString();
    }

    public GlossaryCounts counts(String id) throws LaraException {
        return client.get("/glossaries/" + id + "/counts").as(GlossaryCounts.class);
    }
}
