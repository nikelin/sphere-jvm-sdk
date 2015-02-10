package io.sphere.sdk.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.exceptions.AuthorizationException;
import io.sphere.sdk.exceptions.OldSphereClientException;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/** OAuth tokens returned by the authorization server. */
final class Tokens {
    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    @JsonProperty("expires_in")
    private final Optional<Long> expiresIn;

    public Tokens(String accessToken, String refreshToken, Optional<Long> expiresIn) {
        if (isEmpty(accessToken))
            throw new AuthorizationException("OAuth response must contain an access_token. Was empty.");
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public Optional<Long> getExpiresIn() {
        return expiresIn;
    }

    public static TypeReference<Tokens> typeReference() {
        return new TypeReference<Tokens>() {
            @Override
            public String toString() {
                return "TypeReference<Tokens>";
            }
        };
    }
}
