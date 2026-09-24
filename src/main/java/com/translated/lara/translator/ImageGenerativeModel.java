package com.translated.lara.translator;

/** Generative rendering models that accept text-only image paragraphs. */
public enum ImageGenerativeModel {
    GENERATIVE(ImageTranslationModel.GENERATIVE),
    GENERATIVE_FAST(ImageTranslationModel.GENERATIVE_FAST);

    private final ImageTranslationModel model;

    ImageGenerativeModel(ImageTranslationModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model.toString();
    }

    public static String toString(ImageGenerativeModel model) {
        return model != null ? model.toString() : null;
    }
}
