package com.translated.lara.translator;

import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.TimeoutException;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Memories {

    private final LaraClient client;
    private final long pollingInterval;

    Memories(LaraClient client) {
        this(client, 2000);
    }

    Memories(LaraClient client, long pollingInterval) {
        this.client = client;
        this.pollingInterval = pollingInterval;
    }

    public List<Memory> list() throws LaraException {
        return client.get("/memories").asList(Memory.class);
    }

    public Memory create(String name) throws LaraException {
        return create(name, null);
    }

    public Memory create(String name, String externalId) throws LaraException {
        return client.post("/memories", new HttpParams<>()
                .set("name", name)
                .set("external_id", externalId)
                .build()
        ).as(Memory.class);
    }

    public Memory get(String id) throws LaraException {
        try {
            return client.get("/memories/" + id).as(Memory.class);
        } catch (LaraApiException e) {
            if (e.getHttpCode() == 404) {
                return null;
            }

            throw e;
        }
    }

    public Memory delete(String id) throws LaraException {
        return client.delete("/memories/" + id).as(Memory.class);
    }

    public Memory update(String id, String name) throws LaraException {
        return client.put("/memories/" + id, new HttpParams<>()
                .set("name", name)
                .build()
        ).as(Memory.class);
    }

    public Memory connect(String id) throws LaraException {
        List<Memory> list = connect(Collections.singletonList(id));
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Memory> connect(String... ids) throws LaraException {
        return connect(Arrays.asList(ids));
    }

    public List<Memory> connect(List<String> ids) throws LaraException {
        return client.post("/memories/connect", new HttpParams<>()
                .set("ids", ids)
                .build()
        ).asList(Memory.class);
    }

    public MemoryImport importTmx(String id, File tmx) throws LaraException {
        return importTmx(id, tmx, tmx.getName().toLowerCase().endsWith(".gz"));
    }

    public MemoryImport importTmx(String id, File tmx, boolean gzip) throws LaraException {
        Map<String, Object> params = new HttpParams<>()
                .set("compression", gzip ? "gzip" : null)
                .build();
        Map<String, File> files = new HttpParams<File>()
                .set("tmx", tmx)
                .build();

        return client.post("/memories/" + id + "/import", params, files).as(MemoryImport.class);
    }

    public MemoryImport addTranslation(String id, String source, String target, String sentence, String translation) throws LaraException {
        return addTranslation(id, source, target, sentence, translation, null, null, null);
    }

    public MemoryImport addTranslation(String id, String source, String target, String sentence, String translation, String tuid) throws LaraException {
        return addTranslation(id, source, target, sentence, translation, tuid, null, null);
    }

    public MemoryImport addTranslation(String id, String source, String target, String sentence, String translation, String sentenceBefore, String sentenceAfter) throws LaraException {
        return addTranslation(id, source, target, sentence, translation, null, sentenceBefore, sentenceAfter);
    }

    public MemoryImport addTranslation(String id, String source, String target, String sentence, String translation, String tuid, String sentenceBefore, String sentenceAfter) throws LaraException {
        Map<String, Object> params = translationParams(source, target, sentence, translation, tuid, sentenceBefore, sentenceAfter).build();
        return client.put("/memories/" + id + "/content", params).as(MemoryImport.class);
    }

    public MemoryImport addTranslation(List<String> ids, String source, String target, String sentence, String translation) throws LaraException {
        return addTranslation(ids, source, target, sentence, translation, null, null, null);
    }

    public MemoryImport addTranslation(List<String> ids, String source, String target, String sentence, String translation, String tuid) throws LaraException {
        return addTranslation(ids, source, target, sentence, translation, tuid, null, null);
    }

    public MemoryImport addTranslation(List<String> ids, String source, String target, String sentence, String translation, String sentenceBefore, String sentenceAfter) throws LaraException {
        return addTranslation(ids, source, target, sentence, translation, null, sentenceBefore, sentenceAfter);
    }

    public MemoryImport addTranslation(List<String> ids, String source, String target, String sentence, String translation, String tuid, String sentenceBefore, String sentenceAfter) throws LaraException {
        Map<String, Object> params = translationParams(source, target, sentence, translation, tuid, sentenceBefore, sentenceAfter).set("ids", ids).build();
        return client.put("/memories/content", params).as(MemoryImport.class);
    }

    public MemoryImport deleteTranslation(String id, String source, String target, String sentence, String translation) throws LaraException {
        return deleteTranslation(id, source, target, sentence, translation, null, null, null);
    }

    public MemoryImport deleteTranslation(String id, String source, String target, String sentence, String translation, String tuid) throws LaraException {
        return deleteTranslation(id, source, target, sentence, translation, tuid, null, null);
    }

    public MemoryImport deleteTranslation(String id, String source, String target, String sentence, String translation, String sentenceBefore, String sentenceAfter) throws LaraException {
        return deleteTranslation(id, source, target, sentence, translation, null, sentenceBefore, sentenceAfter);
    }

    public MemoryImport deleteTranslation(String id, String source, String target, String sentence, String translation, String tuid, String sentenceBefore, String sentenceAfter) throws LaraException {
        Map<String, Object> params = translationParams(source, target, sentence, translation, tuid, sentenceBefore, sentenceAfter).build();
        return client.delete("/memories/" + id + "/content", params).as(MemoryImport.class);
    }

    public MemoryImport deleteTranslation(List<String> ids, String source, String target, String sentence, String translation) throws LaraException {
        return deleteTranslation(ids, source, target, sentence, translation, null, null, null);
    }

    public MemoryImport deleteTranslation(List<String> ids, String source, String target, String sentence, String translation, String tuid) throws LaraException {
        return deleteTranslation(ids, source, target, sentence, translation, tuid, null, null);
    }

    public MemoryImport deleteTranslation(List<String> ids, String source, String target, String sentence, String translation, String sentenceBefore, String sentenceAfter) throws LaraException {
        return deleteTranslation(ids, source, target, sentence, translation, null, sentenceBefore, sentenceAfter);
    }

    public MemoryImport deleteTranslation(List<String> ids, String source, String target, String sentence, String translation, String tuid, String sentenceBefore, String sentenceAfter) throws LaraException {
        Map<String, Object> params = translationParams(source, target, sentence, translation, tuid, sentenceBefore, sentenceAfter).set("ids", ids).build();
        return client.delete("/memories/content", params).as(MemoryImport.class);
    }

    private HttpParams<Object> translationParams(String source, String target, String sentence, String translation, String tuid, String sentenceBefore, String sentenceAfter) {
        return new HttpParams<>()
                .set("source", source)
                .set("target", target)
                .set("sentence", sentence)
                .set("translation", translation)
                .set("tuid", tuid)
                .set("sentence_before", sentenceBefore)
                .set("sentence_after", sentenceAfter);
    }

    public MemoryImport getImportStatus(String id) throws LaraException {
        return client.get("/memories/imports/" + id).as(MemoryImport.class);
    }

    public MemoryImport waitForImport(MemoryImport mImport) throws LaraException, InterruptedException {
        return waitForImport(mImport, null, 0);
    }

    public MemoryImport waitForImport(MemoryImport mImport, Consumer<MemoryImport> updateCallback) throws LaraException, InterruptedException {
        return waitForImport(mImport, updateCallback, 0);
    }

    public MemoryImport waitForImport(MemoryImport mImport, long maxWaitTimeMs) throws LaraException, InterruptedException {
        return waitForImport(mImport, null, maxWaitTimeMs);
    }

    public MemoryImport waitForImport(MemoryImport mImport, Consumer<MemoryImport> updateCallback, long maxWaitTimeMs) throws LaraException, InterruptedException {
        long start = System.currentTimeMillis();
        while (mImport.getProgress() < 1.) {
            if (maxWaitTimeMs > 0 && System.currentTimeMillis() - start > maxWaitTimeMs)
                throw new TimeoutException();

            Thread.sleep(this.pollingInterval);

            mImport = getImportStatus(mImport.getId());
            if (updateCallback != null)
                updateCallback.accept(mImport);
        }

        return mImport;
    }

}
