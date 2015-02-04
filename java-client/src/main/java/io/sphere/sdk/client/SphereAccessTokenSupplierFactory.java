package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;

public final class SphereAccessTokenSupplierFactory {
    private SphereAccessTokenSupplierFactory() {
    }

    public static SphereAccessTokenSupplierFactory of() {
        return new SphereAccessTokenSupplierFactory();
    }

    /**
     * Provides a token generator which just returns a fixed token, so the client is usable
     * for the live time of this token.
     *
     * @param token the token which will be passed to the client
     * @return token service
     */
    public SphereAccessTokenSupplier createSupplierOfFixedToken(final String token) {
        return new SphereFixedAccessTokenSupplierImpl(token);
    }

    /**
     * Provides a token generator which tries to always provide a valid token.
     *
     * @param config the configuration to fetch a token
     * @return token service
     */
    public SphereAccessTokenSupplier createSupplierOfAutoRefresh(final SphereAuthConfig config) {
        return SphereAccessTokenSupplierImpl.createAndBeginRefreshInBackground(config, defaultHttpClient());
    }

    //TODO put this into the a base class
    private HttpClient defaultHttpClient() {
        return NingAsyncHttpClientAdapter.of();
    }
}
