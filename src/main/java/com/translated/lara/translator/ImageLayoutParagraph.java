package com.translated.lara.translator;

import java.util.List;

/**
 * Translated image text with layout metadata and optional matches.
 * Its constructors require all layout fields.
 * Use this type for overlay, inpainting, or a model that could select either family.
 * Generative rendering also accepts this type.
 */
public class ImageLayoutParagraph extends ImageParagraph {

    private final ImageBBox bbox;
    private final List<ImageBBox> linesBboxes;
    private final ImageTextInfo textInfo;
    private final String alignment;

    public ImageLayoutParagraph(String text, String translation, ImageBBox bbox, List<ImageBBox> linesBboxes,
                                ImageTextInfo textInfo, String alignment) {
        this(text, translation, null, null, bbox, linesBboxes, textInfo, alignment);
    }

    public ImageLayoutParagraph(String text, String translation, List<NGMemoryMatch> adaptedToMatches,
                                List<NGGlossaryMatch> glossariesMatches, ImageBBox bbox, List<ImageBBox> linesBboxes,
                                ImageTextInfo textInfo, String alignment) {
        super(text, translation, adaptedToMatches, glossariesMatches);
        this.bbox = bbox;
        this.linesBboxes = linesBboxes;
        this.textInfo = textInfo;
        this.alignment = alignment;
    }

    public ImageBBox getBbox() {
        return bbox;
    }

    public List<ImageBBox> getLinesBboxes() {
        return linesBboxes;
    }

    public ImageTextInfo getTextInfo() {
        return textInfo;
    }

    /** @return {@code left}, {@code center}, or {@code right} */
    public String getAlignment() {
        return alignment;
    }
}
