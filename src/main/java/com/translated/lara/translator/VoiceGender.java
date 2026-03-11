package com.translated.lara.translator;

/**
 * Voice gender for audio translation synthesis.
 */
public enum VoiceGender {

    MALE("male"),
    FEMALE("female");

    private final String value;

    VoiceGender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the API string value for the given gender, or null if null.
     */
    public static String toString(VoiceGender gender) {
        return gender != null ? gender.toString() : null;
    }
}
