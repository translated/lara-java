package com.translated.lara.translator;

import java.util.List;

public class DocumentOptions {
    private String[] adaptTo = null;

    public String[] getAdaptTo() {
        return adaptTo;
    }

    public DocumentOptions setAdaptTo(List<String> adaptTo) {
        this.adaptTo = adaptTo != null ? adaptTo.toArray(new String[0]) : null;
        return this;
    }

    public DocumentOptions setAdaptTo(String... adaptTo) {
        this.adaptTo = adaptTo;
        return this;
    }
}
