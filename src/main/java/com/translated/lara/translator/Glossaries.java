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
        return client.get("/v2/glossaries").asList(Glossary.class);
    }

    public Glossary create(String name) throws LaraException {
        return client.post("/v2/glossaries", new HttpParams<>()
                .set("name", name)
                .build()
        ).as(Glossary.class);
    }

    public Glossary get(String id) throws LaraException {
        try {
            return client.get("/v2/glossaries/" + id).as(Glossary.class);
        } catch (LaraApiException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }

            throw e;
        }
    }

    public Glossary delete(String id) throws LaraException {
        return client.delete("/v2/glossaries/" + id).as(Glossary.class);
    }

    public Glossary update(String id, String name) throws LaraException {
        return client.put("/v2/glossaries/" + id, new HttpParams<>()
                .set("name", name)
                .build()
        ).as(Glossary.class);
    }

    public GlossaryShares getShares(String id) throws LaraException {
        return client.get("/v2/glossaries/" + id + "/shares").as(GlossaryShares.class);
    }

    public Glossary addAccountShare(String id) throws LaraException { return addAccountShare(id, null); }

    public Glossary addAccountShare(String id, String name) throws LaraException {
        return client.post("/v2/glossaries/" + id + "/shares", shareName(name)).as(Glossary.class);
    }

    public Glossary renameAccountShare(String id, String name) throws LaraException {
        return client.put("/v2/glossaries/" + id + "/shares", shareName(name)).as(Glossary.class);
    }

    public Glossary revokeAccountShare(String id) throws LaraException {
        return client.delete("/v2/glossaries/" + id + "/shares").as(Glossary.class);
    }

    public Glossary addGroupShare(String id, String groupId) throws LaraException { return addGroupShare(id, groupId, null); }

    public Glossary addGroupShare(String id, String groupId, String name) throws LaraException {
        return client.post("/v2/glossaries/" + id + "/shares/groups/" + groupId, shareName(name)).as(Glossary.class);
    }

    public Glossary renameGroupShare(String id, String groupId, String name) throws LaraException {
        return client.put("/v2/glossaries/" + id + "/shares/groups/" + groupId, shareName(name)).as(Glossary.class);
    }

    public Glossary revokeGroupShare(String id, String groupId) throws LaraException {
        return client.delete("/v2/glossaries/" + id + "/shares/groups/" + groupId).as(Glossary.class);
    }

    private Map<String, Object> shareName(String name) {
        return new HttpParams<>().set("name", name).build();
    }

    public GlossaryImport importFile(String id, File file) throws LaraException {
        return importFile(id, file, new GlossaryImportOptions());
    }

    public GlossaryImport importFile(String id, File file, GlossaryImportOptions options) throws LaraException {
        if (options == null) options = new GlossaryImportOptions();
        boolean gzip = Boolean.TRUE.equals(options.getGzip());
        Map<String, Object> params = new HttpParams<>()
                .set("compression", gzip ? "gzip" : null)
                .set("content_type", options.getContentType().toString())
                .set("callback_url", options.getCallbackUrl())
                .build();
        Map<String, File> files = new HttpParams<File>()
                .set("csv", file)
                .build();

        return client.post("/v2/glossaries/" + id + "/import", params, files, null).as(GlossaryImport.class);
    }

    /**
     * @deprecated Use {@link #importFile(String, File)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv) throws LaraException {
        return importFile(id, csv);
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, String callbackUrl) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions().setCallbackUrl(callbackUrl));
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, boolean gzip) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions().setGzip(gzip));
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, Glossary.Type contentType) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions().setContentType(csvContentType(contentType)));
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, Glossary.Type contentType, String callbackUrl) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions()
                .setContentType(csvContentType(contentType)).setCallbackUrl(callbackUrl));
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, Glossary.Type contentType, boolean gzip) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions()
                .setContentType(csvContentType(contentType)).setGzip(gzip));
    }

    /**
     * @deprecated Use {@link #importFile(String, File, GlossaryImportOptions)} instead.
     */
    @Deprecated
    public GlossaryImport importCsv(String id, File csv, Glossary.Type contentType, boolean gzip, String callbackUrl) throws LaraException {
        return importFile(id, csv, new GlossaryImportOptions()
                .setContentType(csvContentType(contentType)).setGzip(gzip).setCallbackUrl(callbackUrl));
    }

    private Glossary.Type csvContentType(Glossary.Type contentType) {
        if (contentType == Glossary.Type.TBX) {
            throw new IllegalArgumentException("importCsv only supports CSV formats; use importFile for TBX files.");
        }
        return contentType;
    }

    public GlossaryImport getImportStatus(String id) throws LaraException {
        return client.get("/v2/glossaries/imports/" + id).as(GlossaryImport.class);
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

    public String export(String id, Glossary.Type contentType) throws LaraException {
        return export(id, contentType, null);
    }

    public String export(String id, Glossary.Type contentType, String source) throws LaraException {
        HttpParams<Object> params = new HttpParams<>()
                .set("content_type", contentType.toString());
        if (source != null) {
            params.set("source", source);
        }
        return client.get("/v2/glossaries/" + id + "/export?" + params.toQueryString()).toString();
    }

    public GlossaryExport exportAsync(String id, String callbackUrl, Glossary.Type contentType) throws LaraException {
        return exportAsync(id, callbackUrl, contentType, null);
    }

    public GlossaryExport exportAsync(String id, String callbackUrl, Glossary.Type contentType, String source) throws LaraException {
        String endpoint = "/v2/glossaries/" + id + "/export/async";
        HttpParams<Object> params = new HttpParams<>()
                .set("content_type", contentType.toString())
                .set("callback_url", callbackUrl)
                .set("source", source);
        if (params.build() != null) {
            endpoint += "?" + params.toQueryString();
        }
        return client.get(endpoint).as(GlossaryExport.class);
    }

    public GlossaryCounts counts(String id) throws LaraException {
        return client.get("/v2/glossaries/" + id + "/counts").as(GlossaryCounts.class);
    }

    public GlossaryImport addOrReplaceEntry(String glossaryId, List<GlossaryTerm> terms, String guid) throws LaraException {
        HttpParams<Object> params = new HttpParams<>()
                .set("terms", terms);
        if (guid != null) {
            params.set("guid", guid);
        }

        return client.put("/v2/glossaries/" + glossaryId + "/content", params.build()).as(GlossaryImport.class);
    }

    public GlossaryImport addOrReplaceEntry(String glossaryId, List<GlossaryTerm> terms) throws LaraException {
        return addOrReplaceEntry(glossaryId, terms, null);
    }

    public GlossaryImport deleteEntry(String glossaryId, GlossaryTerm term, String guid) throws LaraException {
        HttpParams<Object> params = new HttpParams<>();
        if (guid != null) {
            params.set("guid", guid);
        }
        if (term != null) {
            params.set("term", term);
        }

        return client.delete("/v2/glossaries/" + glossaryId + "/content", params.build()).as(GlossaryImport.class);
    }

    public GlossaryImport deleteEntry(String glossaryId, GlossaryTerm term) throws LaraException {
        return deleteEntry(glossaryId, term, null);
    }

    public GlossaryImport deleteEntry(String glossaryId, String guid) throws LaraException {
        return deleteEntry(glossaryId, null, guid);
    }
}
