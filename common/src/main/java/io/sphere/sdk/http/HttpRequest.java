package io.sphere.sdk.http;

import java.io.File;

public interface HttpRequest extends Requestable {
    HttpMethod getHttpMethod();

    String getPath();

    HttpHeaders getHeaders();

    @Override
    default HttpRequest httpRequest() {
        return this;
    }

    public static HttpRequest of(final HttpMethod httpMethod, final String path) {
        return new HttpRequestImpl(httpMethod, path);
    }

    public static JsonBodyHttpRequest of(final HttpMethod httpMethod, final String path, final String body) {
        return new StringBodyHttpRequestImpl(httpMethod, path, body, HttpHeaders.of());
    }

    public static FileBodyHttpRequest of(final HttpMethod httpMethod, final String path, final File body, final String contentType) {
        return new FileBodyHttpRequestImpl(httpMethod, path, contentType, body);
    }

    public static StringBodyHttpRequest of(final HttpMethod httpMethod, final String path, final String body, final HttpHeaders headers) {
        return new StringBodyHttpRequestImpl(httpMethod, path, body, headers);
    }
}
