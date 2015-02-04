package io.sphere.sdk.http;

import io.sphere.sdk.meta.BuildInfo;
import io.sphere.sdk.models.Base;

import java.util.*;

import static io.sphere.sdk.utils.MapUtils.copyOf;
import static io.sphere.sdk.utils.MapUtils.mapOf;
import static java.util.Collections.unmodifiableMap;

public class HttpHeaders extends Base {
    private final Map<String, String> headers;

    private HttpHeaders(final Map<String, String> headers) {
        final Map<String, String> copy = copyOf(headers);
        copy.put("User-Agent", "SPHERE.IO JVM SDK " + BuildInfo.version());
        this.headers = unmodifiableMap(copy);
    }

    private HttpHeaders(final String key, final String value) {
        this(mapOf(key, value));
    }

    public static HttpHeaders of(final String key, final String value) {
        return new HttpHeaders(key, value);
    }

    public static HttpHeaders of() {
        return new HttpHeaders(Collections.emptyMap());
    }

    public Optional<String> getFlatHeader(final String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public Map<String, String> getHeadersAsMap() {
        return headers;
    }
}
