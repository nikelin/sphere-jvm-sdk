package io.sphere.sdk.exceptions;

/**
 * <span id="exception-summary">Resource could not be deleted since
 * it is referenced by another resource.</span>
 *
 */
public class SphereReferenceExistsException extends BadRequestException {
    private static final long serialVersionUID = 0L;

    public SphereReferenceExistsException(final String requestUrl, final SphereErrorResponse errorResponse) {
//        super(requestUrl, errorResponse); TODO
    }
}
