package io.sphere.sdk.exceptions;


import java.nio.charset.StandardCharsets;
import java.util.Optional;

import io.sphere.sdk.http.*;
import io.sphere.sdk.meta.BuildInfo;
import io.sphere.sdk.utils.JsonUtils;

import java.util.Date;

/** Exception thrown by the Sphere Java client. */
public class OldSphereClientException extends RuntimeException {
    private static final long serialVersionUID = 0L;
    private Optional<String> sphereRequest = Optional.empty();
    private Optional<String> underlyingHttpRequest = Optional.empty();
    private Optional<String> underlyingHttpResponse = Optional.empty();
    private Optional<String> projectKey = Optional.empty();

    protected OldSphereClientException() {}

    public OldSphereClientException(final String message) {
        super(message);
    }

    public OldSphereClientException(final String message, Throwable cause) {
        super(message + ": " + cause.getMessage(), cause);
    }

    public OldSphereClientException(final Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public Optional<String> getSphereRequest() {
        return sphereRequest;
    }

    public void setSphereRequest(final String sphereRequest) {
        this.sphereRequest =  Optional.ofNullable(sphereRequest);
    }

    public Optional<String> getUnderlyingHttpRequest() {
        return underlyingHttpRequest;
    }

    public void setUnderlyingHttpRequest(final String underlyingHttpRequest) {
        this.underlyingHttpRequest = Optional.ofNullable(underlyingHttpRequest);
    }

    public Optional<String> getUnderlyingHttpResponse() {
        return underlyingHttpResponse;
    }

    public void setUnderlyingHttpResponse(final String underlyingHttpResponse) {
        this.underlyingHttpResponse =  Optional.ofNullable(underlyingHttpResponse);
    }

    public Optional<String> getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(final String projectKey) {
        this.projectKey = Optional.ofNullable(projectKey);
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder("\n===== BEGIN EXCEPTION OUTPUT =====").append("\n");
        final String httpRequest = underlyingHttpRequest.orElse("<unknown>");
        return builder.
                append("date: ").append(new Date()).append("\n").
                append("SDK version: ").append(BuildInfo.version()).append("\n").
                append("Java runtime: ").append(System.getProperty("java.version")).append("\n").
                append("cwd: ").append(System.getProperty("user.dir")).append("\n").
                append("project key: ").append(projectKey.orElse("<unknown>")).append("\n").
                append("sphere request: ").append(sphereRequest.orElse("<unknown>")).append("\n").
                append("underlying http request: ").append(httpRequest).append("\n").
                append("underlying http response: ").append(underlyingHttpResponse.orElse("<unknown>")).append("\n").
                append("detailMessage: ").append(super.getMessage()).append("\n").
                append("===== END EXCEPTION OUTPUT =====").toString();
    }

    public void setUnderlyingHttpRequest(final HttpRequestIntent httpRequestIntent) {
        final String body = debugOutputFor(httpRequestIntent);
        final String requestAsString = new StringBuilder(httpRequestIntent.getHttpMethod().toString()).append(" ").append(httpRequestIntent.getPath()).append("\n").append(body).toString();
        setUnderlyingHttpRequest(requestAsString);
    }

    private String debugOutputFor(final HttpRequestIntent httpRequestIntent) {
        final String output;
        if (httpRequestIntent.hasJsonBody()) {
            final StringHttpRequestBody httpRequestBody = (StringHttpRequestBody) httpRequestIntent.getBody().get();
            output = JsonUtils.prettyPrintJsonStringSecureWithFallback(httpRequestBody.getUnderlying());
        } else if(httpRequestIntent.getBody().isPresent()) {
            output = "<binary request body>";
        } else {
            output = "";
        }
        return output;
    }

    public void setUnderlyingHttpResponse(final HttpResponse httpResponse) {
        final String bodyAsDebugString = httpResponse.getResponseBody().map(body -> new String(body, StandardCharsets.UTF_8)).map(bodyAsString -> "body={" + bodyAsString +"}").orElse("no body");
        final String s = "status=" + httpResponse.getStatusCode() + " " + bodyAsDebugString;
        setUnderlyingHttpResponse(s);
    }
}
