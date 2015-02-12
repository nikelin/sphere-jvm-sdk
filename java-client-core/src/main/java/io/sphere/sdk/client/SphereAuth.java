package io.sphere.sdk.client;

import io.sphere.sdk.utils.SphereInternalLogger;

import java.io.Closeable;

import static io.sphere.sdk.utils.SphereInternalLogger.getLogger;

final class SphereAuth {
    static final SphereInternalLogger AUTH_LOGGER = getLogger("oauth");

    private SphereAuth() {
    }

    static void logBirth(final Object o) {
        AUTH_LOGGER.trace(() -> "Create object " + o);
    }

    static void logClose(final Closeable o) {
        AUTH_LOGGER.trace(() -> "Closed object " + o);
    }
}
