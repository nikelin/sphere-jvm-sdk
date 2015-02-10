package io.sphere.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sphere.sdk.exceptions.*;
import io.sphere.sdk.http.*;
import io.sphere.sdk.meta.BuildInfo;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.exceptions.SphereException;
import io.sphere.sdk.utils.JsonUtils;
import io.sphere.sdk.utils.SphereIOUtils;
import io.sphere.sdk.utils.SphereInternalLogger;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.sphere.sdk.utils.SphereInternalLogger.getLogger;

final class SphereClientImpl extends Base implements SphereClient {
    private final ObjectMapper objectMapper = JsonUtils.newObjectMapper();
    private final HttpClient httpClient;
    private final SphereApiConfig config;
    private final SphereAccessTokenSupplier tokenSupplier;


    private SphereClientImpl(final SphereApiConfig config, final SphereAccessTokenSupplier tokenSupplier, final HttpClient httpClient) {
        this.httpClient = httpClient;
        this.config = config;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public <T> CompletableFuture<T> execute(final SphereRequest<T> sphereRequest) {
        final SphereRequest<T> usedClientRequest = new CachedHttpRequestSphereRequest<>(sphereRequest);
        final HttpRequest httpRequest = usedClientRequest
                .httpRequestIntent()
                .plusHeader("User-Agent", "SPHERE.IO JVM SDK " + BuildInfo.version())
                .plusHeader("Authorization", "Bearer " + tokenSupplier.get())
                .prefixPath("/" + config.getProjectKey())
                .toHttpRequest(config.getApiUrl());

        final SphereInternalLogger logger = getLogger(httpRequest);
        logger.debug(() -> usedClientRequest);
        logger.trace(() -> {
            final String output;
            if (httpRequest.getBody().isPresent() && httpRequest.getBody().get() instanceof StringHttpRequestBody) {
                final StringHttpRequestBody body = (StringHttpRequestBody) httpRequest.getBody().get();
                final String unformattedJson = body.getUnderlying();
                output = "send: " + unformattedJson + "\nformatted: " + JsonUtils.prettyPrintJsonStringSecure(unformattedJson);
            } else {
                output = "no request body present";
            }
            return output;
        });
        return httpClient.
                execute(httpRequest).
                thenApply(preProcess(usedClientRequest));
    }

    private <T> Function<HttpResponse, T> preProcess(final SphereRequest<T> sphereRequest) {
        return new Function<HttpResponse, T>() {
            @Override
            public T apply(final HttpResponse httpResponse) {
                final SphereInternalLogger logger = getLogger(httpResponse);
                logger.debug(() -> httpResponse);
                logger.trace(() -> httpResponse.getStatusCode() + "\n" + httpResponse.getResponseBody().map(body -> JsonUtils.prettyPrintJsonStringSecure(bytesToString(body))).orElse("No body present.") + "\n");
                final T result;
                if (isErrorResponse(httpResponse) && !sphereRequest.canHandleResponse(httpResponse)) {
                    result = handleErrors(httpResponse, sphereRequest);
                } else {
                    try {
                        result = sphereRequest.resultMapper().apply(httpResponse);
                    } catch (final JsonException e) {
                        final byte[] bytes = httpResponse.getResponseBody().get();
                        throw new JsonException("Cannot parse " + bytesToString(bytes), e);
                    }
                }
                return result;
            }

        };
    }

    public <T> T handleErrors(final HttpResponse httpResponse, final SphereRequest<T> sphereRequest) {
        SphereErrorResponse errorResponse;
        try {
            if (!httpResponse.getResponseBody().isPresent()) {//the /model/id endpoint does not return JSON on 404
                errorResponse = SphereErrorResponse.of(httpResponse.getStatusCode(), "<no body>", Collections.<SphereError>emptyList());
            } else {
                errorResponse = objectMapper.readValue(httpResponse.getResponseBody().get(), SphereErrorResponse.typeReference());
            }
        } catch (final Exception e) {
            if (isServiceNotAvailable(httpResponse)) {
                throw new ServiceUnavailableException(e);
            } else {
                final SphereException exception = new SphereException("Can't parse backend response.", e);
                fillExceptionWithData(httpResponse, exception, sphereRequest);
                throw exception;
            }
        }
        final SphereException exception;
        if (httpResponse.getStatusCode() == 409) {
            exception = new ConcurrentModificationException(sphereRequest.httpRequestIntent().getPath(), errorResponse);
        } else if(!errorResponse.getErrors().isEmpty() && errorResponse.getErrors().get(0).getCode().equals("ReferenceExists")) {
            exception = new SphereReferenceExistsException(sphereRequest.httpRequestIntent().getPath(), errorResponse);
        } else {
            exception = new SphereException(sphereRequest.httpRequestIntent(), errorResponse);
        }
        fillExceptionWithData(httpResponse, exception, sphereRequest);
        throw exception;
    }

    private boolean isServiceNotAvailable(final HttpResponse httpResponse) {
        return httpResponse.getStatusCode() == 503 || httpResponse.getResponseBody().map(b -> bytesToString(b)).map(s -> s.contains("<h2>Service Unavailable</h2>")).orElse(false);
    }

    private static String bytesToString(final byte[] b) {
        return new String(b, StandardCharsets.UTF_8);
    }

    private static boolean isErrorResponse(final HttpResponse httpResponse) {
        return httpResponse.getStatusCode() / 100 != 2;
    }

    private <T> void fillExceptionWithData(final HttpResponse httpResponse, final SphereException exception, final SphereRequest<T> sphereRequest) {
//        exception.setSphereRequest(sphereRequest.toString());
//        exception.setUnderlyingHttpRequest(sphereRequest.httpRequestIntent());
//        exception.setUnderlyingHttpResponse(httpResponse);
//        exception.setProjectKey(config.getProjectKey());
        //TODO
    }

    @Override
    public void close() {
        SphereIOUtils.closeQuietly(tokenSupplier);
        SphereIOUtils.closeQuietly(httpClient);
    }

    public static SphereClient of(final SphereApiConfig config, final SphereAccessTokenSupplier tokenSupplier, final HttpClient httpClient) {
        return new SphereClientImpl(config, tokenSupplier, httpClient);
    }
}
