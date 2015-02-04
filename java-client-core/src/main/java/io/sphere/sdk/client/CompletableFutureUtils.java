package io.sphere.sdk.client;

import java.util.concurrent.*;

final class CompletableFutureUtils {
    private CompletableFutureUtils() {
    }

    public static <T> CompletableFuture<T> fulfilled(final T object) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.complete(object);
        return future;
    }
}
