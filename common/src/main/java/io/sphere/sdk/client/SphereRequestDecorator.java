package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.http.HttpResponse;

import java.util.function.Function;

public abstract class SphereRequestDecorator<T> implements SphereRequest<T> {
    private final SphereRequest<T> delegate;

    protected SphereRequestDecorator(final SphereRequest<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canHandleResponse(final HttpResponse response) {
        return delegate.canHandleResponse(response);
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return delegate.httpRequestIntent();
    }

    @Override
    public Function<HttpResponse, T> resultMapper() {
        return delegate.resultMapper();
    }
}
