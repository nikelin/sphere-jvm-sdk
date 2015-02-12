package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.models.Base;

import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.client.SphereAuth.*;

final class OnDemandSphereAccessTokenSupplier extends Base implements SphereAccessTokenSupplier {
    private final TokensSupplier tokensSupplier;
    private boolean isClosed = false;

    private OnDemandSphereAccessTokenSupplier(final SphereAuthConfig config, final HttpClient httpClient, final boolean closeHttpClient) {
        tokensSupplier = TokensSupplierImpl.of(config, httpClient, closeHttpClient);
        logBirth(this);
    }

    @Override
    public synchronized void close() {
        if (!isClosed) {
            tokensSupplier.close();
            isClosed = true;
            logClose(this);
        }
    }

    @Override
    public CompletableFuture<String> get() {
        return tokensSupplier.get().thenApply(Tokens::getAccessToken);
    }

    public static OnDemandSphereAccessTokenSupplier of(final SphereAuthConfig config, final HttpClient httpClient, final boolean closeHttpClient) {
        return new OnDemandSphereAccessTokenSupplier(config, httpClient, closeHttpClient);
    }
}
