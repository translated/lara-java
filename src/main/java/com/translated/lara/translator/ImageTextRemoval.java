package com.translated.lara.translator;

public enum ImageTextRemoval {
    OVERLAY("overlay"),
    INPAINTING("inpainting");

    private final String value;

    ImageTextRemoval(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String toString(ImageTextRemoval removal) {
        return removal != null ? removal.toString() : null;
    }
}