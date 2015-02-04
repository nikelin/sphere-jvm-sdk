package io.sphere.sdk.http;

public interface StringBodyHttpRequest extends HttpRequest {
    @Override
    HttpMethod getHttpMethod();

    @Override
    String getPath();

    String getBody();
}
