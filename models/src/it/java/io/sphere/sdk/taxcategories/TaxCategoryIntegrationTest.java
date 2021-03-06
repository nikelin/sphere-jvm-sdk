package io.sphere.sdk.taxcategories;

import io.sphere.sdk.taxcategories.commands.TaxCategoryCreateCommand;
import io.sphere.sdk.taxcategories.commands.TaxCategoryDeleteCommand;
import io.sphere.sdk.taxcategories.queries.TaxCategoryQuery;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.neovisionaries.i18n.CountryCode.DE;
import static java.util.Arrays.asList;

public class TaxCategoryIntegrationTest extends IntegrationTest {

    @After
    @Before
    public void setUp() throws Exception {
        execute(TaxCategoryQuery.of().byName("German tax")).getResults()
                .forEach(taxCategory -> execute(TaxCategoryDeleteCommand.of(taxCategory)));
    }

    @Test
    public void demoForDeletion() throws Exception {
        final TaxCategory taxCategory = createTaxCategory();
        final TaxCategory deletedTaxCategory = execute(TaxCategoryDeleteCommand.of(taxCategory));
    }

    private TaxCategory createTaxCategory() {
        final TaxRate taxRate = TaxRate.of("GERMAN default tax", 0.19, false, DE);
        final TaxCategoryDraft taxCategoryDraft = TaxCategoryDraft.of("German tax", "Normal-Steuersatz", asList(taxRate));
        final TaxCategory taxCategory = execute(TaxCategoryCreateCommand.of(taxCategoryDraft));
        return taxCategory;
    }
}
