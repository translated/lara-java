package com.translated.lara.translator;

import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.net.ClientOptions;
import com.translated.lara.net.LaraClient;

import java.util.List;

public class Translator {

    private final LaraClient client;
    public final Memories memories;

    public Translator(Credentials credentials) {
        this(credentials, null);
    }

    public Translator(Credentials credentials, ClientOptions options) {
        this.client = new LaraClient(credentials, options);
        this.memories = new Memories(client);
    }

    public List<String> getLanguages() throws LaraException {
        return client.get("/languages").asList(String.class);
    }

    public TextResult translate(String text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(String text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }

    public TextResult translate(List<String> text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(List<String> text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }

    public TextResult translate(String[] text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translate(String[] text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }

    public TextResult translateBlocks(List<TextBlock> text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translateBlocks(List<TextBlock> text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }

    public TextResult translateBlocks(TextBlock[] text, String source, String target) throws LaraException {
        return translateAny(text, source, target, null);
    }

    public TextResult translateBlocks(TextBlock[] text, String source, String target, TranslateOptions options) throws LaraException {
        return translateAny(text, source, target, options);
    }

    private TextResult translateAny(Object text, String source, String target, TranslateOptions options) throws LaraException {
        return client.post("/translate",
                TranslateOptions.toParams(options)
                        .set("source", source)
                        .set("target", target)
                        .set("q", text)
                        .build()
        ).as(TextResult.class);
    }

}
