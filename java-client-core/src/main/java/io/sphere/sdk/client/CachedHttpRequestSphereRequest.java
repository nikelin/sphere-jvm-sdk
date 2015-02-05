package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.models.Base;

import java.util.function.Function;

final class CachedHttpRequestSphereRequest<T> extends Base implements SphereRequest<T> {
    private final SphereRequest<T> delegate;
    private final HttpRequestIntent httpRequest;

    CachedHttpRequestSphereRequest(final SphereRequest<T> delegate) {
        this.delegate = delegate;
        this.httpRequest = delegate.httpRequestIntent();
    }

    @Override
    public Function<HttpResponse, T> resultMapper() {
        return delegate.resultMapper();
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return httpRequest;
    }

    @Override
    public boolean canHandleResponse(final HttpResponse response) {
        return delegate.canHandleResponse(response);
    }
}
