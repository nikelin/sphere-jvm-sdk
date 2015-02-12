package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

final class OneTimeSphereAccessTokenSupplier extends Base implements SphereAccessTokenSupplier {
    private final SphereAccessTokenSupplier delegate;
    private boolean shouldCloseAutomatically = false;
    private boolean isClosed = false;
    private Optional<CompletableFuture<String>> tokenFuture = Optional.empty();

    private OneTimeSphereAccessTokenSupplier(final SphereAccessTokenSupplier delegate, final boolean shouldCloseAutomatically) {
        this.delegate = delegate;
        this.shouldCloseAutomatically = shouldCloseAutomatically;
    }

    @Override
    public synchronized void close() {
        if (shouldCloseAutomatically && !isClosed) {
            delegate.close();
            isClosed = true;
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
            shouldCloseAutomatically = false;
        }
        return result;
    }

    public static SphereAccessTokenSupplier of(final SphereAccessTokenSupplier delegate, final boolean shouldCloseAutomatically) {
        return new OneTimeSphereAccessTokenSupplier(delegate, shouldCloseAutomatically);
    }
}
