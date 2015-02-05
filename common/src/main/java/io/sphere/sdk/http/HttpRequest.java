package io.sphere.sdk.http;

import java.util.Optional;

public interface HttpRequest {
    HttpMethod getHttpMethod();

    String getUrl();

    HttpHeaders getHeaders();

    Optional<HttpRequestBody> getBody();

    static HttpRequest of(final HttpMethod httpMethod, final String url, final String contentType, final String body) {
        return of(httpMethod, url, HttpHeaders.of("Content-Type", contentType), Optional.of(StringHttpRequestBody.of(body)));
    }

    static HttpRequest of(final HttpMethod httpMethod, final String url, final HttpHeaders headers, final Optional<HttpRequestBody> body) {
        return new HttpRequestImpl(httpMethod, url, headers, body);
    }
}
