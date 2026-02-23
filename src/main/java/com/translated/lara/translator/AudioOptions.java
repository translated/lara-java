package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

import java.util.List;

/**
 * Options that control how audio translations are generated.
 * <p>
 * This class is used for in-memory configuration of audio translation behavior
 * and is typically embedded inside {@link Audio}.
 */
public class AudioOptions {
    private String[] adaptTo = null;
    private String[] glossaries = null;
    private Boolean noTrace = null;
    private TranslationStyle style = null;

    /**
     * Returns the list of adaptation targets (for example customer IDs or domains).
     */
    public String[] getAdaptTo() {
        return adaptTo;
    }

    /**
     * Sets the adaptation targets using a {@link java.util.List}.
     *
     * @param adaptTo list of adaptation targets; may be {@code null}
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setAdaptTo(List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    /**
     * Sets the adaptation targets.
     *
     * @param adaptTo array of adaptation targets; may be {@code null}
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }

    /**
     * Returns the list of glossary identifiers to be applied during translation.
     */
    public String[] getGlossaries() {
        return glossaries;
    }

    /**
     * Sets the glossaries using a {@link java.util.List} of identifiers.
     *
     * @param glossaries list of glossary identifiers; may be {@code null}
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setGlossaries(List<String> glossaries) {
        this.glossaries = glossaries != null ? glossaries.toArray(new String[0]) : null;
        return this;
    }

    /**
     * Sets the glossaries identifiers.
     *
     * @param glossaries array of glossary identifiers; may be {@code null}
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setGlossaries(String... glossaries) {
        this.glossaries = glossaries;
        return this;
    }

    /**
     * Returns whether request/response tracing should be disabled.
     */
    public Boolean getNoTrace() {
        return noTrace;
    }

    /**
     * Enables or disables request/response tracing.
     *
     * @param noTrace if {@code true}, tracing will be disabled; if {@code false} or {@code null},
     *                default tracing behavior applies
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setNoTrace(Boolean noTrace) {
        this.noTrace = noTrace;
        return this;
    }

    /**
     * Returns the translation style to be applied.
     */
    public TranslationStyle getStyle() {
        return style;
    }

    /**
     * Sets the translation style to be applied.
     *
     * @param style translation style; may be {@code null}
     * @return this {@code AudioOptions} instance for method chaining
     */
    public AudioOptions setStyle(TranslationStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Converts the configured options into HTTP parameters
     * to be sent to the LARA audio translation endpoint.
     *
     * @return a {@link HttpParams} instance containing all non-null options
     */
    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("adapt_to", adaptTo);
        params.set("glossaries", glossaries);
        params.set("style", TranslationStyle.toString(style));

        return params;
    }
}
