package io.sphere.sdk.client;

import io.sphere.sdk.exceptions.AuthorizationException;
import io.sphere.sdk.utils.Validation;

final class ValidationE<T> extends Validation<T, AuthorizationException> {

    //TODO make private
    public ValidationE(T value, final AuthorizationException exception) {
        super(value, exception);
    }

    /**
     * Creates a new erroneous result.
     * @param exception the error of the result
     * @param <T> the type of the possible value, but absent value
     * @return the result
     */
    public static <T> ValidationE<T> error(final AuthorizationException exception) {
        return new ValidationE<>(null, exception);
    }
}

