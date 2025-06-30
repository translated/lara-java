package com.translated.lara.translator;

import java.util.Map;

public class GlossaryCounts {
    private Map<String, Integer> unidirectional;
    private Integer multidirectional;

    public GlossaryCounts() {}

    public GlossaryCounts(Map<String, Integer> unidirectional, Integer multidirectional) {
        this.unidirectional = unidirectional;
        this.multidirectional = multidirectional;
    }

    public Map<String, Integer> getUnidirectional() {
        return unidirectional;
    }

    public void setUnidirectional(Map<String, Integer> unidirectional) {
        this.unidirectional = unidirectional;
    }

    public Integer getMultidirectional() {
        return multidirectional;
    }

    public void setMultidirectional(Integer multidirectional) {
        this.multidirectional = multidirectional;
    }

    @Override
    public String toString() {
        return "GlossaryCounts{" +
                "unidirectional=" + (unidirectional != null ? unidirectional.toString() : "null") +
                ", multidirectional=" + multidirectional +
                '}';
    }
}
