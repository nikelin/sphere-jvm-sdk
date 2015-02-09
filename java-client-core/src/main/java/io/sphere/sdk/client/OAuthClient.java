package io.sphere.sdk.client;

import io.sphere.sdk.exceptions.AuthorizationException;
import io.sphere.sdk.exceptions.JsonException;
import io.sphere.sdk.http.*;
import io.sphere.sdk.utils.JsonUtils;
import io.sphere.sdk.utils.MapUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.http.HttpMethod.POST;
import static java.lang.String.format;

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
        try {
            final String usernamePassword = format("%s:%s", config.getClientId(), config.getClientSecret());
            final String encodedString = Base64.getEncoder().encodeToString(usernamePassword.getBytes(StandardCharsets.UTF_8));
            final HttpHeaders httpHeaders = HttpHeaders
                    .of("Authorization", "Basic " + encodedString)
                            .plus("Content-Type", "application/x-www-form-urlencoded");
            final FormUrlEncodedHttpRequestBody body = FormUrlEncodedHttpRequestBody.of(MapUtils.mapOf("grant_type", "client_credentials", "scope", format("manage_project:%s", config.getProjectKey())));
            final HttpRequest request = HttpRequest.of(POST, config.getAuthUrl() + "/oauth/token", httpHeaders, Optional.of(body));

            return httpClient.execute(request).thenApply(OAuthClient::parseResponse);
        } catch (JsonException e) {
            throw new AuthorizationException(e);
        }
    }


    /** Parses Tokens from a response from the backend authorization service.
     *  @param response Response from the authorization service.
     */
    private static Tokens parseResponse(final HttpResponse response) {
        if (response.getStatusCode() != 200 || !response.getResponseBody().isPresent()) {
            throw new AuthorizationException(response.toString());
        }
        return JsonUtils.readObject(Tokens.typeReference(), response.getResponseBody().get());
    }

    public static OAuthClient of(final SphereAuthConfig config, final HttpClient httpClient) {
        return new OAuthClient(config, httpClient);
    }
}
