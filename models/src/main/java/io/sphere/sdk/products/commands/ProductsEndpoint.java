package io.sphere.sdk.products.commands;

import io.sphere.sdk.client.JsonEndpoint;
import io.sphere.sdk.products.Product;

final class ProductsEndpoint {
    static final JsonEndpoint<Product> ENDPOINT = JsonEndpoint.of(Product.typeReference(), "/products");
}
