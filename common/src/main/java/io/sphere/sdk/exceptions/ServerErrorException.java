package io.sphere.sdk.exceptions;

/**
 * SPHERE.IO answered with a HTTP response code of &gt;= 500.
 */
public class ServerErrorException extends SphereServiceException {
    static final long serialVersionUID = 0L;

    public ServerErrorException(final Throwable cause) {
        //TODO
    }
}
