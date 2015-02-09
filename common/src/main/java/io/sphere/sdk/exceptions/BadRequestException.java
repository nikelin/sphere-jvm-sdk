package io.sphere.sdk.exceptions;

/**
 * HTTP code 400 response from SPHERE.IO.
 *
 */
public class BadRequestException extends SphereServiceException {
    private static final long serialVersionUID = 0L;

    public BadRequestException(final String message, final Exception cause) {
        super(message, cause);
    }

    public BadRequestException() {
    }
}
