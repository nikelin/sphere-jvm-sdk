package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.models.Base;

import java.util.concurrent.CompletableFuture;

final class OnDemandSphereAccessTokenSupplier extends Base implements SphereAccessTokenSupplier {
    private final TokensSupplier tokensSupplier;
    private boolean isClosed = false;

    private OnDemandSphereAccessTokenSupplier(final SphereAuthConfig config, final HttpClient httpClient, final boolean closeHttpClient) {
        tokensSupplier = TokensSupplier.of(config, httpClient, closeHttpClient);
    }

    @Override
    public synchronized void close() {
        if (!isClosed) {
            tokensSupplier.close();
            isClosed = true;
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
