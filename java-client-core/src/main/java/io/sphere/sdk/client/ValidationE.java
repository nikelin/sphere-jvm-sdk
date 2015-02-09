package io.sphere.sdk.client;

import io.sphere.sdk.exceptions.OldSphereClientException;
import io.sphere.sdk.utils.Validation;

/** Validation with the error type being {@link io.sphere.sdk.exceptions.OldSphereClientException}. */
final class ValidationE<T> extends Validation<T, OldSphereClientException> {

    //TODO make private
    public ValidationE(T value, OldSphereClientException exception) {
        super(value, exception);
    }

    /**
     * Creates a new erroneous result.
     * @param exception the error of the result
     * @param <T> the type of the possible value, but absent value
     * @return the result
     */
    public static <T> ValidationE<T> error(OldSphereClientException exception) {
        return new ValidationE<>(null, exception);
    }
}

