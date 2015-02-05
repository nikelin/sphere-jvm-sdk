package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

final class SphereConstantAccessTokenSupplierImpl extends Base implements SphereAccessTokenSupplier {
    private final String token;

    SphereConstantAccessTokenSupplierImpl(final String token) {
        this.token = token;
    }

    @Override
    public String get() {
        return token;
    }

    @Override
    public void close() {
    }
}
