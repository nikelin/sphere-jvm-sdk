package io.sphere.sdk.exceptions;

import io.sphere.sdk.http.HttpRequestIntent;

/**
 * <span id="exception-summary">Base class for all exceptions related to the SDK.</span>
 *
 */
public class SphereException extends RuntimeException {
    static final long serialVersionUID = 0L;

    public SphereException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SphereException() {
    }

    public SphereException(final String path, final SphereErrorResponse errorResponse) {
        //TODO
    }

    public SphereException(final String message) {
        //TODO
    }

    public SphereException(final Throwable cause) {
        super(cause);
    }

    public SphereException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        //TODO
    }

    public SphereException(final HttpRequestIntent httpRequestIntent, final SphereErrorResponse errorResponse) {

    }
}
