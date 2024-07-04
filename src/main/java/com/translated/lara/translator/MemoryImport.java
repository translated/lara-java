package com.translated.lara.translator;

import java.util.Objects;

public class MemoryImport {

    private final String id;
    private final long begin;
    private final long end;
    private final int channel;
    private final long size;
    private final float progress;

    public MemoryImport(String id, long begin, long end, int channel, long size, float progress) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.channel = channel;
        this.size = size;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    public int getChannel() {
        return channel;
    }

    public long getSize() {
        return size;
    }

    public float getProgress() {
        return progress;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof MemoryImport)) return false;
        MemoryImport that = (MemoryImport) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemoryImport{id='" + id + "'}";
    }

}
