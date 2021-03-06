package io.sphere.sdk.products;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.Instant;
import java.util.Optional;
import io.sphere.sdk.models.DefaultModelImpl;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.taxcategories.TaxCategory;

class ProductImpl extends DefaultModelImpl<Product> implements Product {
    private final Reference<ProductType> productType;
    private final ProductCatalogData masterData;
    private final Optional<Reference<TaxCategory>> taxCategory;

    @JsonCreator
    ProductImpl(final String id, final long version, final Instant createdAt, final Instant lastModifiedAt,
                final Reference<ProductType> productType, final ProductCatalogData masterData, final Optional<Reference<TaxCategory>> taxCategory) {
        super(id, version, createdAt, lastModifiedAt);
        this.productType = productType;
        this.masterData = masterData;
        this.taxCategory = taxCategory;
    }

    @Override
    public Reference<ProductType> getProductType() {
        return productType;
    }

    @Override
    public ProductCatalogData getMasterData() {
        return masterData;
    }

    @Override
    public Optional<Reference<TaxCategory>> getTaxCategory() {
        return taxCategory;
    }
}
