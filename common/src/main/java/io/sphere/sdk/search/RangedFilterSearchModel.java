package io.sphere.sdk.search;

import java.util.Optional;

import static java.util.Arrays.asList;

public class RangedFilterSearchModel<T, V extends Comparable<? super V>> extends FilterSearchModel<T, V> {

    RangedFilterSearchModel(final Optional<? extends SearchModel<T>> parent, final Optional<String> pathSegment, final TypeSerializer<V> typeSerializer) {
        super(parent, pathSegment, typeSerializer);
    }

    @Override
    public FilterExpression<T> is(final V value) {
        return super.is(value);
    }

    @Override
    public FilterExpression<T> isIn(final Iterable<V> values) {
        return super.isIn(values);
    }

    public FilterExpression<T> isWithin(final FilterRange<V> range) {
        return isWithin(asList(range));
    }

    public FilterExpression<T> isWithin(final Iterable<FilterRange<V>> ranges) {
        return new RangeFilterExpression<>(this, typeSerializer, ranges);
    }

    public FilterExpression<T> isWithin(final V lowerEndpoint, final V upperEndpoint) {
        return isWithin(FilterRange.of(lowerEndpoint, upperEndpoint));
    }

    public FilterExpression<T> isGreaterThanOrEqualTo(final V value) {
        return isWithin(FilterRange.atLeast(value));
    }

    public FilterExpression<T> isLessThanOrEqualTo(final V value) {
        return isWithin(FilterRange.atMost(value));
    }

    // NOT SUPPORTED YET
/*
    public FilterExpression<T> isGreaterThan(final V value) {
        return isWithin(Range.greaterThan(value));
    }

    public FilterExpression<T> isLessThan(final V value) {
        return isWithin(Range.lessThan(value));
    }
*/
}
