package io.sphere.sdk.exceptions;

/**
 *
 * <span id="exception-summary">Exception thrown when SPHERE.IO responds<br>with a status code other than HTTP 2xx.</span>
 *
 */
public class SphereServiceException extends SphereException {
    static final long serialVersionUID = 0L;

    //TODO status code

//TODO
    public SphereServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SphereServiceException() {
//TODO
    }

    public SphereServiceException(final String message) {
        //TODO
    }

    public SphereServiceException(final Throwable cause) {
        //TODO
    }
}
