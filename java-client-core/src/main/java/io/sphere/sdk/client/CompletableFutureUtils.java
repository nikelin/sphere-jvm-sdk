package io.sphere.sdk.client;

import java.util.concurrent.*;

final class CompletableFutureUtils {
    private CompletableFutureUtils() {
    }

    public static <T> CompletableFuture<T> successful(final T object) {
        return CompletableFuture.completedFuture(object);
    }

    public static <T> CompletableFuture<T> failed(final Throwable e) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }

    public static <T> void transferResult(final CompletableFuture<T> futureSource,
                                          final CompletableFuture<T> futureTarget) {
        futureSource.whenComplete((result, throwable) -> {
            final boolean isSuccessful = throwable == null;
            if (isSuccessful) {
                futureTarget.complete(result);
            } else {
                futureTarget.completeExceptionally(throwable);
            }
        });
    }
}
