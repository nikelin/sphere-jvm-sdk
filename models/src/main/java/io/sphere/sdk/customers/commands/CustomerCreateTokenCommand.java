package io.sphere.sdk.customers.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.commands.CommandImpl;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.utils.JsonUtils;

import static io.sphere.sdk.http.HttpMethod.POST;

public class CustomerCreateTokenCommand extends CommandImpl<CustomerToken> {
    private final String email;

    private CustomerCreateTokenCommand(final String customerEmail) {
        this.email = customerEmail;
    }

    public static CustomerCreateTokenCommand of(final String customerEmail) {
        return new CustomerCreateTokenCommand(customerEmail);
    }

    @Override
    protected TypeReference<CustomerToken> typeReference() {
        return CustomerToken.typeReference();
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return HttpRequestIntent.of(POST, "/customers/password-token", JsonUtils.toJson(this));
    }

    public String getEmail() {
        return email;
    }
}
