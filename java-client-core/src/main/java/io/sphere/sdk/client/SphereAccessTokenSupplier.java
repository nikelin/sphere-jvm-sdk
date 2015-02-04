package io.sphere.sdk.client;

import java.io.Closeable;
import java.util.function.Supplier;

/** Provides an OAuth token for accessing protected Sphere HTTP API endpoints.
 *
 * There a no guarantees concerning the token providing mechanism.
 */
public interface SphereAccessTokenSupplier extends Closeable, Supplier<String> {
    /** Returns the OAuth access token. */
    public String get();

    public void close();
}
