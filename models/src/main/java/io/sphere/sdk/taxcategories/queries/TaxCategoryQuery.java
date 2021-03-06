package io.sphere.sdk.taxcategories.queries;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.queries.DefaultModelQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryDsl;
import io.sphere.sdk.taxcategories.TaxCategory;

/**
 {@doc.gen summary tax categories}
 */
public class TaxCategoryQuery extends DefaultModelQuery<TaxCategory> {
    private TaxCategoryQuery(){
        super(TaxCategoriesEndpoint.ENDPOINT.endpoint(), resultTypeReference());
    }

    public static TypeReference<PagedQueryResult<TaxCategory>> resultTypeReference() {
        return new TypeReference<PagedQueryResult<TaxCategory>>(){
            @Override
            public String toString() {
                return "TypeReference<PagedQueryResult<TaxCategory>>";
            }
        };
    }

    public QueryDsl<TaxCategory> byName(final String name) {
        return withPredicate(model().name().is(name));
    }

    public static TaxCategoryQuery of() {
        return new TaxCategoryQuery();
    }

    public static TaxCategoryQueryModel model() {
        return TaxCategoryQueryModel.get();
    }
}
