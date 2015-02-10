package io.sphere.sdk.exceptions;

import io.sphere.sdk.http.HttpRequestIntent;

import java.util.Optional;

/**
 * <span id="exception-summary">Base class for all exceptions related to the SDK.</span>
 *
 */
public class SphereException extends RuntimeException {
    static final long serialVersionUID = 0L;

    private Optional<String> sphereRequest = Optional.empty();
    private Optional<String> underlyingHttpRequest = Optional.empty();
    private Optional<String> underlyingHttpResponse = Optional.empty();
    private Optional<String> projectKey = Optional.empty();

    public SphereException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SphereException() {
    }

    public SphereException(final String message) {
        //TODO
    }

    public SphereException(final Throwable cause) {
        super(cause);
    }

    public SphereException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        //TODO
    }

    public SphereException(final HttpRequestIntent httpRequestIntent, final SphereErrorResponse errorResponse) {

    }

    public Optional<String> getProjectKey() {
        return projectKey;
    }

    public Optional<String> getSphereRequest() {
        return sphereRequest;
    }

    public Optional<String> getUnderlyingHttpRequest() {
        return underlyingHttpRequest;
    }

    public Optional<String> getUnderlyingHttpResponse() {
        return underlyingHttpResponse;
    }

    public void setProjectKey(final Optional<String> projectKey) {
        this.projectKey = projectKey;
    }

    public void setSphereRequest(final Optional<String> sphereRequest) {
        this.sphereRequest = sphereRequest;
    }

    public void setUnderlyingHttpRequest(final Optional<String> underlyingHttpRequest) {
        this.underlyingHttpRequest = underlyingHttpRequest;
    }

    public void setUnderlyingHttpResponse(final Optional<String> underlyingHttpResponse) {
        this.underlyingHttpResponse = underlyingHttpResponse;
    }

    public void setProjectKey(final String projectKey) {
        this.projectKey = Optional.of(projectKey);
    }

    public void setSphereRequest(final String sphereRequest) {
        this.sphereRequest = Optional.of(sphereRequest);
    }

    public void setUnderlyingHttpRequest(final String underlyingHttpRequest) {
        this.underlyingHttpRequest = Optional.of(underlyingHttpRequest);
    }

    public void setUnderlyingHttpResponse(final String underlyingHttpResponse) {
        this.underlyingHttpResponse = Optional.of(underlyingHttpResponse);
    }
}
