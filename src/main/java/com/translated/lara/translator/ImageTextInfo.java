package com.translated.lara.translator;

import com.google.gson.annotations.SerializedName;

/** Text direction and colors used when rendering an image paragraph. */
public class ImageTextInfo {

    private final String direction;
    @SerializedName("text_color")
    private final String textColor;
    @SerializedName("background_color")
    private final String backgroundColor;

    /**
     * @param direction text direction: {@code ltr}, {@code rtl}, or {@code ttb}
     * @param textColor text color
     * @param backgroundColor background color
     */
    public ImageTextInfo(String direction, String textColor, String backgroundColor) {
        this.direction = direction;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public String getDirection() {
        return direction;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
