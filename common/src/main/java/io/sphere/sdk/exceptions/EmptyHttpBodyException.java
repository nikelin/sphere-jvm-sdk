package io.sphere.sdk.exceptions;

import io.sphere.sdk.http.HttpResponse;

/**
 * An empty HTTP body from SPHERE.IO.
 *
 */
public class EmptyHttpBodyException extends SphereServiceException {
    private static final long serialVersionUID = 0L;

    public EmptyHttpBodyException(final HttpResponse httpResponse) {
        super("There is no response body in " + httpResponse);
    }
}
