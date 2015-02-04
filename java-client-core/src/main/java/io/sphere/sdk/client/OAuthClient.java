package io.sphere.sdk.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import io.sphere.sdk.http.*;
import io.sphere.sdk.utils.JsonUtils;

import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.http.HttpMethod.POST;

final class OAuthClient {
    private final HttpClient httpClient;
    private final SphereAuthConfig config;

    private OAuthClient(final SphereAuthConfig config, final HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    /** Asynchronously gets access and refresh tokens for given user from the authorization server
     *  using the Resource owner credentials flow. */
    public CompletableFuture<Tokens> getTokensForClient() {
        final String body = String.format("grant_type=client_credentials&scope=manage_project:%s", config.getProjectKey());
        try {
            final String urlEncodedBody = URLEncoder.encode(body, "UTF-8");
            final StringBodyHttpRequest request = HttpRequest.of(POST, "/oauth/token", urlEncodedBody, HttpHeaders.of("Content-Type", "application/x-www-form-urlencoded"));
            return httpClient.execute(config.getAuthUrl(), request).thenApply(OAuthClient::parseResponse);
        } catch (final UnsupportedEncodingException e) {
            throw new AuthorizationException(e);
        }
    }


    /** Parses Tokens from a response from the backend authorization service.
     *  @param response Response from the authorization service.
     */
    private static Tokens parseResponse(final HttpResponse response) {
        if (response.getStatusCode() != 200 && !response.getResponseBody().isPresent()) {
            throw new AuthorizationException(response.toString());
        }
        return JsonUtils.readObject(Tokens.typeReference(), response.getResponseBody().get());
    }

    public static OAuthClient of(final SphereAuthConfig config, final HttpClient httpClient) {
        return new OAuthClient(config, httpClient);
    }
}
