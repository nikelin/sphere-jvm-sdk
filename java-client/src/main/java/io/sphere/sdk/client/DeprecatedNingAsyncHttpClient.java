package io.sphere.sdk.client;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import io.sphere.sdk.http.*;
import io.sphere.sdk.meta.BuildInfo;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

final class DeprecatedNingAsyncHttpClient extends Base implements HttpClient {

    private final SphereAccessTokenSupplier sphereAccessTokenSupplier;
    private final AsyncHttpClient asyncHttpClient;
    private final SphereApiConfig config;

    public DeprecatedNingAsyncHttpClient(final SphereApiConfig config, final SphereAccessTokenSupplier sphereAccessTokenSupplier) {
        this.config = config;
        asyncHttpClient = new AsyncHttpClient();
        this.sphereAccessTokenSupplier = sphereAccessTokenSupplier;
    }

    public DeprecatedNingAsyncHttpClient(final SphereClientConfig config) {
        asyncHttpClient = new AsyncHttpClient();
        this.sphereAccessTokenSupplier = null;
        this.config = config;
    }

    @Override
    public <T> CompletableFuture<HttpResponse> execute(final String baseUrl, final Requestable requestable) {
        final Request request = asNingRequest(baseUrl, requestable);
        try {
            final CompletableFuture<Response> future = CompletableFutureUtils.wrap(asyncHttpClient.executeRequest(request));
            return future.thenApply((Response response) -> {
                try {
                    final byte[] responseBodyAsBytes = response.getResponseBodyAsBytes();
                    Optional<byte[]> body = responseBodyAsBytes.length > 0 ? Optional.of(responseBodyAsBytes) : Optional.empty();
                    return HttpResponse.of(response.getStatusCode(), body, Optional.of(requestable.httpRequest()));
                } catch (IOException e) {
                    throw new RuntimeException(e);//TODO unify exception handling, to sphere exception
                }
            });
        } catch (final IOException e) {
            throw new RuntimeException(e);//TODO unify exception handling, to sphere exception
        }
    }

    /* package scope for testing */
    <T> Request asNingRequest(final String baseUrl, final Requestable requestable) {
        final HttpRequest request = requestable.httpRequest();
        final RequestBuilder builder = new RequestBuilder()
                .setUrl(baseUrl + request.getPath())
                .setMethod(request.getHttpMethod().toString())
                .setHeader("User-Agent", "SPHERE.IO JVM SDK " + BuildInfo.version())
                .setHeader("Authorization", "Bearer " + sphereAccessTokenSupplier.get());

        if (request instanceof JsonBodyHttpRequest) {
            builder.setBodyEncoding(StandardCharsets.UTF_8.name())
                    .setBody(((JsonBodyHttpRequest) request).getBody());
        } else if (request instanceof StringBodyHttpRequest) {
            builder.setBodyEncoding(StandardCharsets.UTF_8.name())
                    .setBody(((StringBodyHttpRequest) request).getBody());
        } else if (request instanceof FileBodyHttpRequest) {
            final FileBodyHttpRequest binRequest = (FileBodyHttpRequest) request;
            builder.setBody(binRequest.getBody());
        }
        return builder.build();
    }

    @Override
    public void close() {
        sphereAccessTokenSupplier.close();
        asyncHttpClient.close();
    }
}
