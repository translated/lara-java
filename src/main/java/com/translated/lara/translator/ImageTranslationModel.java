package com.translated.lara.translator;

/**
 * Image translation model used to render translated text on the image.
 */
public enum ImageTranslationModel {
    OVERLAY("overlay"),
    INPAINTING("inpainting"),
    GENERATIVE("generative"),
    GENERATIVE_FAST("generative_fast");

    private final String value;

    ImageTranslationModel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ImageTranslationModel model) {
        return model != null ? model.toString() : null;
    }
}
