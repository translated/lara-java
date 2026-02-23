package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

/**
 * Options for image translation requests.
 */
public class ImageTranslateOptions {

    private String[] adaptTo = null;
    private String[] glossaries = null;
    private Boolean noTrace = null;
    private TranslationStyle style = null;
    private ImageTextRemoval textRemoval = null;

    /**
     * Gets the list of domains, brands or styles to adapt the translation to.
     *
     * @return an array of adapt-to identifiers, or {@code null} if not set
     */
    public String[] getAdaptTo() {
        return adaptTo;
    }

    /**
     * Sets the list of domains, brands or styles to adapt the translation to.
     *
     * @param adaptTo a list of adapt-to identifiers, or {@code null}
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setAdaptTo(java.util.List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    /**
     * Sets the list of domains, brands or styles to adapt the translation to.
     *
     * @param adaptTo one or more adapt-to identifiers
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }

    /**
     * Gets the glossary identifiers to use for the translation.
     *
     * @return an array of glossary identifiers, or {@code null} if not set
     */
    public String[] getGlossaries() {
        return glossaries;
    }

    /**
     * Sets the glossary identifiers to use for the translation.
     *
     * @param glossaries a list of glossary identifiers, or {@code null}
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setGlossaries(java.util.List<String> glossaries) {
        this.glossaries = glossaries != null ? glossaries.toArray(new String[0]) : null;
        return this;
    }

    /**
     * Sets the glossary identifiers to use for the translation.
     *
     * @param glossaries one or more glossary identifiers
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setGlossaries(String... glossaries) {
        this.glossaries = glossaries;
        return this;
    }

    /**
     * Indicates whether trace information should be disabled.
     *
     * @return {@code Boolean.TRUE} to disable trace, {@code Boolean.FALSE} to keep it,
     * or {@code null} if not specified
     */
    public Boolean getNoTrace() {
        return noTrace;
    }

    /**
     * Sets whether trace information should be disabled.
     *
     * @param noTrace {@code true} to disable trace, {@code false} to keep it, or {@code null}
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setNoTrace(Boolean noTrace) {
        this.noTrace = noTrace;
        return this;
    }

    /**
     * Gets the translation style to apply.
     *
     * @return the translation style, or {@code null} if not set
     */
    public TranslationStyle getStyle() {
        return style;
    }

    /**
     * Sets the translation style to apply.
     *
     * @param style the translation style
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setStyle(TranslationStyle style) {
        this.style = style;
        return this;
    }


    /**
     * Gets the text removal strategy to apply to the input image.
     *
     * @return the text removal strategy, or {@code null} if not set
     */
    public ImageTextRemoval getTextRemoval() {
        return textRemoval;
    }

    /**
     * Sets the text removal strategy to apply to the input image.
     *
     * @param textRemoval the text removal strategy
     * @return this options instance for chaining
     */
    public ImageTranslateOptions setTextRemoval(ImageTextRemoval textRemoval) {
        this.textRemoval = textRemoval;
        return this;
    }

    /**
     * Converts these options into HTTP parameters for the image translation API.
     *
     * @return an {@link HttpParams} instance containing the configured options
     */
    public HttpParams<Object> toParams() {
        HttpParams<Object> params = new HttpParams<>();
        params.set("adapt_to", adaptTo);
        params.set("glossaries", glossaries);
        params.set("style", TranslationStyle.toString(style));
        params.set("text_removal", ImageTextRemoval.toString(textRemoval));
        return params;
    }
}
