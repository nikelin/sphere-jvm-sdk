package io.sphere.sdk.shippingmethods.commands;

import io.sphere.sdk.client.JsonEndpoint;
import io.sphere.sdk.shippingmethods.ShippingMethod;

final class ShippingMethodEndpoint {
    static final JsonEndpoint<ShippingMethod> ENDPOINT = JsonEndpoint.of(ShippingMethod.typeReference(), "/shipping-methods");
}
