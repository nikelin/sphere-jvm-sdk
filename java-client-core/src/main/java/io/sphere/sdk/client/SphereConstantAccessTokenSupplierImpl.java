package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.client.SphereAuth.*;

final class SphereConstantAccessTokenSupplierImpl extends Base implements SphereAccessTokenSupplier {
    private final CompletableFuture<String> token;

    SphereConstantAccessTokenSupplierImpl(final String token) {
        this.token = CompletableFutureUtils.successful(token);
        logBirth(this);
    }

    SphereConstantAccessTokenSupplierImpl(final CompletableFuture<String> token) {
        this.token = token;
    }

    @Override
    public CompletableFuture<String> get() {
        return token;
    }

    @Override
    public void close() {
        logClose(this);
    }
}
