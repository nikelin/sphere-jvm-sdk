package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.client.SphereAuth.*;

final class OneTimeSphereAccessTokenSupplier extends Base implements SphereAccessTokenSupplier {
    private final SphereAccessTokenSupplier delegate;
    private final boolean shouldCloseAutomatically;
    private boolean isClosed = false;
    private Optional<CompletableFuture<String>> tokenFuture = Optional.empty();

    private OneTimeSphereAccessTokenSupplier(final SphereAccessTokenSupplier delegate, final boolean shouldCloseAutomatically) {
        this.delegate = delegate;
        this.shouldCloseAutomatically = shouldCloseAutomatically;
        logBirth(this);
    }

    @Override
    public synchronized void close() {
        if (shouldCloseAutomatically && !isClosed) {
            delegate.close();
            isClosed = true;
            logClose(this);
        }
    }

    @Override
    public final synchronized CompletableFuture<String> get() {
        return tokenFuture.orElseGet(() -> {
            final CompletableFuture<String> tokenFuture = fetchToken();
            this.tokenFuture = Optional.of(tokenFuture);
            return tokenFuture;
        });
    }

    private CompletableFuture<String> fetchToken() {
        final CompletableFuture<String> result = delegate.get();
        if (shouldCloseAutomatically) {
            result.whenComplete((a, b) -> close());
        }
        return result;
    }

    public static SphereAccessTokenSupplier of(final SphereAccessTokenSupplier delegate, final boolean shouldCloseAutomatically) {
        return new OneTimeSphereAccessTokenSupplier(delegate, shouldCloseAutomatically);
    }
}
