package io.sphere.sdk.exceptions;

/**
 * Problems on parsing JSON.
 *
 */
public class JsonParseException extends JsonException {
    private static final long serialVersionUID = 4954925590077093841L;

    public JsonParseException(final Exception cause) {
        super(cause);
    }

    public JsonParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
