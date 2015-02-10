package io.sphere.sdk.exceptions;

public class UnauthorizedException extends ClientErrorException {
    private static final long serialVersionUID = 0L;

    public UnauthorizedException() {
        super(401);
    }
}
