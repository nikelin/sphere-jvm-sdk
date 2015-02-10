package io.sphere.sdk.exceptions;

import java.io.IOException;

/**
 * Exception thrown when the Sphere authorization service responds with other status code than HTTP 200 OK.
 *
 */
public class AuthorizationException extends SphereException {
    private static final long serialVersionUID = 0L;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    public AuthorizationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
