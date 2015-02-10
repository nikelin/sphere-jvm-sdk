package io.sphere.sdk.client;

import java.util.concurrent.*;

final class CompletableFutureUtils {
    private CompletableFutureUtils() {
    }

    public static <T> CompletableFuture<T> successful(final T object) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.complete(object);
        return future;
    }

    public static <T> CompletableFuture<T> failed(final Throwable e) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }
}
