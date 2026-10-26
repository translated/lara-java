package com.translated.lara.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionResult<T> {
    private final List<T> items;
    private final long total;
    private final Long page;
    private final Integer size;

    public CollectionResult(List<T> items, long total, Long page, Integer size) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<T> getItems() {
        return items;
    }

    /** Total number of matching resources. */
    public long getTotal() {
        return total;
    }

    /** Current page (1-based), or null for an unpaginated response. */
    public Long getPage() {
        return page;
    }

    /** Page size, or null for an unpaginated response. */
    public Integer getSize() {
        return size;
    }
}
