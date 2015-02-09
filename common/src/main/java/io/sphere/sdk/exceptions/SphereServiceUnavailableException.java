package io.sphere.sdk.exceptions;

/**
 * The SPHERE.IO API is currently not available.
 *
 */
public class SphereServiceUnavailableException extends SphereException {
    private static final long serialVersionUID = 0L;

    //TODO HTTP code 503

    public SphereServiceUnavailableException(final Throwable cause) {
        super(cause);
    }
}
