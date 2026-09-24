package com.translated.lara.translator;

import com.google.gson.annotations.SerializedName;

/** A quadrilateral with integer {@code [x, y]} coordinate pairs. */
public class ImageBBox {

    @SerializedName("top_left")
    private final int[] topLeft;
    @SerializedName("top_right")
    private final int[] topRight;
    @SerializedName("bottom_right")
    private final int[] bottomRight;
    @SerializedName("bottom_left")
    private final int[] bottomLeft;

    public ImageBBox(int[] topLeft, int[] topRight, int[] bottomRight, int[] bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    public int[] getTopLeft() {
        return topLeft;
    }

    public int[] getTopRight() {
        return topRight;
    }

    public int[] getBottomRight() {
        return bottomRight;
    }

    public int[] getBottomLeft() {
        return bottomLeft;
    }
}
