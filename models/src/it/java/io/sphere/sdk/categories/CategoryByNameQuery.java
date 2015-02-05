package io.sphere.sdk.categories;

import java.util.function.Function;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.*;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.utils.JsonUtils;

import java.util.Locale;

import static io.sphere.sdk.utils.UrlUtils.urlEncode;

/**
 * This is an example of hard coding queries without using helper classes.
 * It is better to use the helpers concerning existing parts and escaping.
 */
public class CategoryByNameQuery extends Base implements Query<Category> {
    private final Locale locale;
    private final String name;

    public CategoryByNameQuery(final Locale locale, final String name) {
        this.locale = locale;
        this.name = name;
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return HttpRequestIntent.of(HttpMethod.GET, "/categories" + urlEncode("name(" + locale.toLanguageTag() + "=\"" + StringQuerySortingModel.escape(name) + "\")"));
    }

    @Override
    public Function<HttpResponse, PagedQueryResult<Category>> resultMapper() {
        return httpResponse -> JsonUtils.readObject(new TypeReference<PagedQueryResult<Category>>() {
        }, httpResponse.getResponseBody().get());
    }
}
