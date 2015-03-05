package io.sphere.sdk.orders;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.CustomLineItem;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.*;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ImportOrderBuilder extends Base implements Builder<ImportOrder> {
    private Optional<String> orderNumber = Optional.empty();
    private Optional<String> customerId = Optional.empty();
    private Optional<String> customerEmail = Optional.empty();
    private List<ImportLineItem> lineItems = Collections.emptyList();
    private List<CustomLineItem> customLineItems = Collections.emptyList();
    private MonetaryAmount totalPrice;
    private TaxedPrice taxedPrice;
    private Address shippingAddress;
    private Optional<Address> billingAddress = Optional.empty();
    private Optional<Reference<CustomerGroup>> customerGroup = Optional.empty();
    private Optional<CountryCode> country = Optional.empty();
    private OrderState orderState;
    private Optional<ShipmentState> shipmentState = Optional.empty();
    private Optional<PaymentState> paymentState = Optional.empty();
    private Optional<OrderShippingInfo> shippingInfo = Optional.empty();
    private Instant completedAt;

    private ImportOrderBuilder(final MonetaryAmount totalPrice, final TaxedPrice taxedPrice, final Instant completedAt, final OrderState orderState, final Address shippingAddress) {
        this.completedAt = completedAt;
        this.orderState = orderState;
        this.shippingAddress = shippingAddress;
        this.taxedPrice = taxedPrice;
        this.totalPrice = totalPrice;
    }

    public ImportOrderBuilder orderNumber(final Optional<String> orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public ImportOrderBuilder orderNumber(final String orderNumber) {
        return orderNumber(Optional.of(orderNumber));
    }

    public ImportOrderBuilder customerId(final Optional<String> customerId) {
        this.customerId = customerId;
        return this;
    }

    public ImportOrderBuilder customerId(final String customerId) {
        return customerId(Optional.of(customerId));
    }

    public ImportOrderBuilder customerEmail(final Optional<String> customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public ImportOrderBuilder customerEmail(final String customerEmail) {
        return customerEmail(Optional.of(customerEmail));
    }

    public ImportOrderBuilder lineItems(final List<ImportLineItem> ancestors) {
        this.lineItems = lineItems;
        return this;
    }

    public ImportOrderBuilder customLineItems(final List<CustomLineItem> ancestors) {
        this.customLineItems = customLineItems;
        return this;
    }

    public ImportOrderBuilder totalPrice(final MonetaryAmount totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public ImportOrderBuilder taxedPrice(final TaxedPrice taxedPrice) {
        this.taxedPrice = taxedPrice;
        return this;
    }

    public ImportOrderBuilder shippingAddress(final Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public ImportOrderBuilder billingAddress(final Optional<Address> billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public ImportOrderBuilder billingAddress(final Address billingAddress) {
        return billingAddress(Optional.of(billingAddress));
    }

    public ImportOrderBuilder customerGroup(final Optional<Reference<CustomerGroup>> customerGroup) {
        this.customerGroup = customerGroup;
        return this;
    }

    public ImportOrderBuilder customerGroup(final Referenceable<CustomerGroup> customerGroup) {
        return customerGroup(Optional.of(customerGroup.toReference()));
    }

    public ImportOrderBuilder country(final Optional<CountryCode> country) {
        this.country = country;
        return this;
    }

    public ImportOrderBuilder country(final CountryCode country) {
        return country(Optional.of(country));
    }

    public ImportOrderBuilder orderState(final OrderState orderState) {
        this.orderState = orderState;
        return this;
    }

    public ImportOrderBuilder shipmentState(final Optional<ShipmentState> shipmentState) {
        this.shipmentState = shipmentState;
        return this;
    }

    public ImportOrderBuilder shipmentState(final ShipmentState shipmentState) {
        return shipmentState(Optional.of(shipmentState));
    }

    public ImportOrderBuilder paymentState(final Optional<PaymentState> paymentState) {
        this.paymentState = paymentState;
        return this;
    }

    public ImportOrderBuilder paymentState(final PaymentState paymentState) {
        return paymentState(Optional.of(paymentState));
    }

    public ImportOrderBuilder shippingInfo(final Optional<OrderShippingInfo> shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public ImportOrderBuilder shippingInfo(final OrderShippingInfo shippingInfo) {
        return shippingInfo(Optional.of(shippingInfo));
    }

    public ImportOrderBuilder completedAt(final Instant completedAt) {
        this.completedAt = completedAt;
        return this;
    }

    public static ImportOrderBuilder of(final MonetaryAmount totalPrice, final TaxedPrice taxedPrice, final Instant completedAt, final OrderState orderState, final Address shippingAddress) {
        return new ImportOrderBuilder(totalPrice, taxedPrice, completedAt, orderState, shippingAddress);
    }

    @Override
    public ImportOrder build() {
        return new ImportOrderImpl(billingAddress, orderNumber, customerId, customerEmail, lineItems, customLineItems, totalPrice, taxedPrice, shippingAddress, customerGroup, country, orderState, shipmentState, paymentState, shippingInfo, completedAt);
    }
}
