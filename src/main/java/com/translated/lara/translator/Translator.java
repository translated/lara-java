package com.translated.lara.translator;

import com.translated.lara.Version;
import com.translated.lara.authentication.AccessKey;
import com.translated.lara.authentication.AuthToken;
import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.ClientOptions;
import com.translated.lara.net.ClientResponse;
import com.translated.lara.net.HttpParams;
import com.translated.lara.net.LaraClient;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Translator {

    protected final LaraClient client;
    public final Memories memories;
    public final Documents documents;
    public final Glossaries glossaries;
    public final Styleguides styleguides;
    public final ImageTranslator images;
    public final AudioTranslator audio;

    public Translator(AccessKey accessKey) {
        this(accessKey, new ClientOptions());
    }
    public Translator(AccessKey accessKey, ClientOptions options) {
        this.client = new LaraClient(accessKey, options);
        this.memories = new Memories(client);
        this.documents = new Documents(client);
        this.glossaries = new Glossaries(client);
        this.styleguides = new Styleguides(client);
        this.images = new ImageTranslator(client);
        this.audio = new AudioTranslator(client);
    }

    public Translator(AuthToken authToken) {
        this(authToken, new ClientOptions());
    }
    public Translator(AuthToken authToken, ClientOptions options) {
        this.client = new LaraClient(authToken, options);
        this.memories = new Memories(client);
        this.documents = new Documents(client);
        this.glossaries = new Glossaries(client);
        this.styleguides = new Styleguides(client);
        this.images = new ImageTranslator(client);
        this.audio = new AudioTranslator(client);
    }

    public List<String> getLanguages() throws LaraException {
        return client.get("/v2/languages").asList(String.class);
    }

    public String version() {
        return Version.get();
    }

    public DetectResult detect(String text) throws LaraException {
        return detectAny(text, null, null);
    }
    public DetectResult detect(String text, String hint) throws LaraException {
        return detectAny(text, hint, null);
    }
    public DetectResult detect(String text, String hint, List<String> passlist) throws LaraException {
        return detectAny(text, hint, passlist);
    }
    public DetectResult detect(String text, String hint, String[] passlist) throws LaraException {
        return detectAny(text, hint, Arrays.asList(passlist));
    }

    public DetectResult detect(String[] text) throws LaraException {
        return detectAny(text, null, null);
    }
    public DetectResult detect(String[] text, String hint) throws LaraException {
        return detectAny(text, hint, null);
    }
    public DetectResult detect(String[] text, String hint, List<String> passlist) throws LaraException {
        return detectAny(text, hint, passlist);
    }
    public DetectResult detect(String[] text, String hint, String[] passlist) throws LaraException {
        return detectAny(text, hint, Arrays.asList(passlist));
    }

    public DetectResult detect(List<String> text) throws LaraException {
        return detectAny(text, null, null);
    }
    public DetectResult detect(List<String> text, String hint) throws LaraException {
        return detectAny(text, hint, null);
    }
    public DetectResult detect(List<String> text, String hint, List<String> passlist) throws LaraException {
        return detectAny(text, hint, passlist);
    }
    public DetectResult detect(List<String> text, String hint, String[] passlist) throws LaraException {
        return detectAny(text, hint, Arrays.asList(passlist));
    }

    public DetectResult detectAny(Object text, String hint, Collection<String> passlist) throws LaraException {
        HttpParams<Object> params = new HttpParams<>();
        params.set("q", text);
        if (hint != null) {
            params.set("hint", hint);
        }
        if (passlist != null && !passlist.isEmpty()) {
            params.set("passlist", passlist);
        }

        return client.post("/v2/detect/language", params.build()).as(DetectResult.class);
    }

    public ProfanityDetectResult detectProfanities(String text, String language, ContentType contentType) throws LaraException {
        HttpParams<Object> params = new HttpParams<>();
        params.set("text", text);
        params.set("language", language);
        params.set("content_type", contentType.toString());

        return client.post("/v2/detect/profanities", params.build()).as(ProfanityDetectResult.class);
    }

    public TextResult translate(String text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(String text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }
    public TextResult translate(String text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        return translateAny(text, source, target, options, callback);
    }

    public TextResult translate(List<String> text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(List<String> text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }
    public TextResult translate(List<String> text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        return translateAny(text, source, target, options, callback);
    }

    public TextResult translate(String[] text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(String[] text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }
    public TextResult translate(String[] text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        return translateAny(text, source, target, options, callback);
    }

    public TextResult translateBlocks(List<TextBlock> text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translateBlocks(List<TextBlock> text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }
    public TextResult translateBlocks(List<TextBlock> text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        return translateAny(text, source, target, options, callback);
    }

    public TextResult translateBlocks(TextBlock[] text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translateBlocks(TextBlock[] text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }
    public TextResult translateBlocks(TextBlock[] text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        return translateAny(text, source, target, options, callback);
    }

    protected TextResult translateAny(Object text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options, null);
    }

    protected TextResult translateAny(Object text, String source, String target, TranslateOptions options, Consumer<TextResult> callback) throws LaraException {
        HttpParams<Object> params = options == null ? new HttpParams<>() : options.toParams();

        Map<String, String> headers = new HashMap<>();
        if (options != null) {
            if (options.getHeaders() != null) {
                options.getHeaders().forEach((name, value) -> {
                    if (value != null) {
                        headers.put(name, value.toString());
                    }
                });
            }

            if (Boolean.TRUE.equals(options.getNoTrace())) {
                headers.put("X-No-Trace", "true");
            }
        }

        try (Stream<ClientResponse> responseStream = client.postAndGetStream("/translate", params
                .set("source", source)
                .set("target", target)
                .set("q", text)
                .build(),
                headers)) {

            TextResult lastResult = null;
            Iterator<ClientResponse> iterator = responseStream.iterator();
            while (iterator.hasNext()) {
                lastResult = iterator.next().as(TextResult.class);

                if (callback != null) {
                    callback.accept(lastResult);
                }
            }

            if (lastResult == null) {
                throw new LaraApiException(500, "StreamingError", "No translation result received");
            }

            return lastResult;
        }
    }

}
