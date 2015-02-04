package io.sphere.sdk.client;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import io.sphere.sdk.http.*;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

final class NingAsyncHttpClientAdapter extends Base implements HttpClient {
    private final AsyncHttpClient asyncHttpClient;

    NingAsyncHttpClientAdapter(final AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    @Override
    public <T> CompletableFuture<HttpResponse> execute(final String baseUrl, final Requestable requestable) {
        final Request request = asNingRequest(baseUrl, requestable.httpRequest());
        try {
            final CompletableFuture<Response> future = CompletableFutureUtils.wrap(asyncHttpClient.executeRequest(request));
            return future.thenApply((Response response) -> {
                    final byte[] responseBodyAsBytes = getResponseBodyAsBytes(response);
                    Optional<byte[]> body = responseBodyAsBytes.length > 0 ? Optional.of(responseBodyAsBytes) : Optional.empty();
                    return HttpResponse.of(response.getStatusCode(), body, Optional.of(requestable.httpRequest()));
            });
        } catch (final IOException e) {
            throw new HttpException(e);
        }
    }

    private byte[] getResponseBodyAsBytes(final Response response) {
        try {
            return response.getResponseBodyAsBytes();
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    /* package scope for testing */
    <T> Request asNingRequest(final String baseUrl, final HttpRequest request) {
        final RequestBuilder builder = new RequestBuilder()
                .setUrl(baseUrl + request.getPath())
                .setMethod(request.getHttpMethod().toString());

        request.getHeaders().getHeadersAsMap().forEach((name, value) -> builder.setHeader(name, value));

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
        asyncHttpClient.close();
    }

    public static NingAsyncHttpClientAdapter of() {
        return of(new AsyncHttpClient());
    }

    public static NingAsyncHttpClientAdapter of(final AsyncHttpClient asyncHttpClient) {
        return new NingAsyncHttpClientAdapter(asyncHttpClient);
    }
}
