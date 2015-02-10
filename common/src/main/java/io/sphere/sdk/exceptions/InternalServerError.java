package io.sphere.sdk.exceptions;

public class InternalServerError extends ServerErrorException {
    static final long serialVersionUID = 0L;

    public InternalServerError(final Throwable cause) {
        super(cause);
    }
}
