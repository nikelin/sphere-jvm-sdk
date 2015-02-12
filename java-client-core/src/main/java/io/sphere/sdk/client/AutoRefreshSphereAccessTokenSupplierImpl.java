package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.models.Base;


import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static io.sphere.sdk.utils.SphereIOUtils.closeQuietly;

/**
 *  Holds OAuth access tokenCache for accessing protected Sphere HTTP API endpoints.
 *  Refreshes the access token as needed automatically.
 */
final class AutoRefreshSphereAccessTokenSupplierImpl extends Base implements SphereAccessTokenSupplier {
    private final AuthActor authActor;
    private volatile Optional<CompletableFuture<String>> cache = Optional.empty();

    private AutoRefreshSphereAccessTokenSupplierImpl(final SphereAuthConfig config, final HttpClient httpClient, final boolean closeHttpClient) {
        final TokensSupplier internalTokensSupplier = TokensSupplier.of(config, httpClient, closeHttpClient);
        authActor = new AuthActor(internalTokensSupplier, this);
        authActor.tell(new AuthActor.FetchTokenMessage());
    }

    @Override
    public CompletableFuture<String> get() {
        return cache.orElseGet(() -> {
            final CompletableFuture<String> callerTokenFuture = new CompletableFuture<>();
            authActor.tell(new AuthActor.TokenRequestMessage(callerTokenFuture));
            return callerTokenFuture;
        });
    }

    @Override
    public void close() {
        closeQuietly(authActor);
    }

    public static SphereAccessTokenSupplier createAndBeginRefreshInBackground(final SphereAuthConfig config, final HttpClient httpClient, final boolean closeHttpClient) {
        return new AutoRefreshSphereAccessTokenSupplierImpl(config, httpClient, closeHttpClient);
    }

    public void setToken(final String accessToken) {
        cache = Optional.of(CompletableFutureUtils.successful(accessToken));
    }
}
