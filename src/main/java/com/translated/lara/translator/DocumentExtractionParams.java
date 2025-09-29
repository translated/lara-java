package com.translated.lara.translator;

import com.translated.lara.net.HttpParams;

/**
 * Abstract base class for extraction parameters by file format.
 */
public abstract class DocumentExtractionParams {

    /**
     * Convert to HTTP parameters for API requests.
     * @return HttpParams containing the extraction parameters
     */
    public abstract HttpParams<Object> toParams();
}

