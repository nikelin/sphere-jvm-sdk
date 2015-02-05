package io.sphere.sdk.http;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface HttpClient extends Closeable {
    <T> CompletableFuture<HttpResponse> execute(String baseUrl, HttpRequest request);

    void close();
}
