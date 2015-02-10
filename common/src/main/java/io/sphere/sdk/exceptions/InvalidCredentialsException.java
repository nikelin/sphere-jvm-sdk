package io.sphere.sdk.exceptions;

import io.sphere.sdk.client.SphereAuthConfig;

public class InvalidCredentialsException extends AuthorizationException {
    private static final long serialVersionUID = 0L;

    public InvalidCredentialsException(final SphereAuthConfig config) {
        super("Invalid credentials for " + config.getProjectKey() + " on " + config.getAuthUrl());
    }
}
