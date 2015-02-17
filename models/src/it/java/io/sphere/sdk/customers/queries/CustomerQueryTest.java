package io.sphere.sdk.customers.queries;

import io.sphere.sdk.customergroups.CustomerGroupFixtures;
import io.sphere.sdk.customers.*;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.SetCustomerGroup;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.queries.Predicate;
import io.sphere.sdk.queries.Query;
import io.sphere.sdk.queries.SortDirection;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.*;

import java.util.List;
import java.util.function.Function;

import static io.sphere.sdk.customergroups.CustomerGroupFixtures.b2cCustomerGroup;
import static org.fest.assertions.Assertions.assertThat;
import static io.sphere.sdk.test.SphereTestUtils.*;
import static java.util.stream.Collectors.toList;

public class CustomerQueryTest extends IntegrationTest {
    public static final String FIRST_NAME = "John";
    private static Customer customer;
    private static Customer distraction;

    @BeforeClass
    public static void setUpCustomer() throws Exception {
        customer = createCustomer(FIRST_NAME, "Smith");
        distraction = createCustomer("Missy", "Jones");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        customer = null;
        distraction = null;
    }

    @Test
    public void email() throws Exception {
        check((model) -> model.email().is(customer.getEmail()));
    }

    @Test
    public void firstName() throws Exception {
        check((model) -> model.firstName().is(customer.getFirstName()));
    }

    @Test
    public void lastName() throws Exception {
        check((model) -> model.lastName().is(customer.getLastName()));
    }

    @Test
    public void defaultShippingAddressId() throws Exception {
        check((model) -> model.defaultShippingAddressId().is(customer.getDefaultShippingAddressId().get()));
    }

    @Test
    public void defaultBillingAddressId() throws Exception {
        check((model) -> model.defaultBillingAddressId().is(customer.getDefaultBillingAddressId().get()));
    }

    //deactivated because of bug in the platform
//    @Test
//    public void isEmailVerified() throws Exception {
//        check((model) -> model.isEmailVerified().is(false), false);
//    }

    @Test
    public void externalId() throws Exception {
        check((model) -> model.externalId().is(customer.getExternalId().get()));
    }

    @Test
    public void customerGroup() throws Exception {
        check((model) -> model.customerGroup().is(customer.getCustomerGroup().get()), false);
    }

    private void check(final Function<CustomerQueryModel, Predicate<Customer>> f) {
        check(f, false);
    }

    private void check(final Function<CustomerQueryModel, Predicate<Customer>> f, final boolean checkDistraction) {
        final CustomerQueryModel model = CustomerQuery.model();
        final Predicate<Customer> predicate = f.apply(model);
        final Query<Customer> query = CustomerQuery.of().withPredicate(predicate).withSort(model.createdAt().sort(SortDirection.DESC));
        final List<Customer> results = execute(query).getResults();
        final List<String> ids = results.stream().map(x -> x.getId()).collect(toList());
        assertThat(ids).contains(customer.getId());
        if (checkDistraction) {
            assertThat(ids.contains(distraction.getId())).isFalse();
        }
    }

    private static Customer createCustomer(final String firstName, final String lastName) {
        final CustomerName customerName = CustomerName.ofFirstAndLastName(firstName, lastName);
        final CustomerDraft draft = CustomerDraft.of(customerName, randomEmail(CustomerQueryTest.class), "secret")
                .withExternalId(randomString()+firstName);
        final CustomerSignInResult signInResult = execute(CustomerCreateCommand.of(draft));
        final Customer initialCustomer = signInResult.getCustomer();

        final Customer updatedCustomer = execute(CustomerUpdateCommand.of(initialCustomer, asList(AddAddress.of(randomAddress()), SetCustomerGroup.of(b2cCustomerGroup(client())))));

        final SetDefaultShippingAddress shippingAddressAction = SetDefaultShippingAddress.of(updatedCustomer.getAddresses().get(0));
        final SetDefaultBillingAddress billingAddressAction = SetDefaultBillingAddress.of(updatedCustomer.getAddresses().get(0));
        return execute(CustomerUpdateCommand.of(updatedCustomer, asList(shippingAddressAction, billingAddressAction)));
    }
}