package com.translated.lara.translator;

/** Query options for listing memories, glossaries and styleguides with optional name filtering and pagination. */
public class ResourceQueryOptions {
    private String search;
    private Long page;
    private Integer size;

    /**
     * Filter resources by their displayed name using a case- and accent-insensitive
     * substring match. Spaces are preserved. Enables pagination when provided.
     */
    public String getSearch() {
        return search;
    }

    public ResourceQueryOptions setSearch(String search) {
        this.search = search;
        return this;
    }

    /** Page number (1-based). Enables pagination when provided. */
    public Long getPage() {
        return page;
    }

    public ResourceQueryOptions setPage(long page) {
        this.page = page;
        return this;
    }

    /** Sets the page number, or clears it when null. */
    public ResourceQueryOptions setPage(Long page) {
        this.page = page;
        return this;
    }

    /** Page size (1-200). Enables pagination when provided. */
    public Integer getSize() {
        return size;
    }

    public ResourceQueryOptions setSize(Integer size) {
        this.size = size;
        return this;
    }
}
