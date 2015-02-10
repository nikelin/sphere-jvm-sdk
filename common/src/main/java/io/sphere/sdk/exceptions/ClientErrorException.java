package io.sphere.sdk.exceptions;

/**
 * Exceptions for SPHERE.IO http responses with an error code 4xx.
 */
public abstract class ClientErrorException extends SphereServiceException {
    private static final long serialVersionUID = 0L;

    public ClientErrorException(final int statusCode) {
        super(statusCode);
    }
}
