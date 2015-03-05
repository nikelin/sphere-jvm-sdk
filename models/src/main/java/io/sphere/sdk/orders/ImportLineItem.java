package io.sphere.sdk.orders;

import io.sphere.sdk.carts.ItemState;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.taxcategories.TaxRate;

import java.util.List;
import java.util.Optional;

/**
 *
 * @see io.sphere.sdk.orders.ImportLineItemBuilder
 */
public interface ImportLineItem {
    Optional<LocalizedStrings> getName();

    Price getPrice();

    Optional<String> getProductId();

    long getQuantity();

    List<ItemState> getState();

    Optional<Reference<Channel>> getSupplyChannel();

    TaxRate getTaxRate();

    ImportProductVariant getVariant();
}
