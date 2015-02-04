package io.sphere.sdk.http;

class StringBodyHttpRequestImpl extends HttpRequestImpl implements JsonBodyHttpRequest, StringBodyHttpRequest {
    private final String body;

    StringBodyHttpRequestImpl(final HttpMethod httpMethod, final String path, final String body) {
        this(httpMethod, path, body, HttpHeaders.of());
    }

    StringBodyHttpRequestImpl(final HttpMethod httpMethod, final String path, final String body, final HttpHeaders headers) {
        super(httpMethod, path, headers);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }
}
