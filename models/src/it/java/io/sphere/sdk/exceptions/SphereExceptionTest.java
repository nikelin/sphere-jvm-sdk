package io.sphere.sdk.exceptions;

import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.sphere.sdk.http.HttpMethod.POST;

public class SphereExceptionTest extends IntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidJsonInHttpRequestIntent() throws Throwable {
        executing(() -> TestSphereRequest.of(HttpRequestIntent.of(POST, "/categories", "{invalidJson :)")))
                .resultsInA(JsonException.class);
    }

    @Test
    public void gatewayTimeout() throws Throwable {
        aHttpResponseWithCode(504).resultsInA(GatewayTimeoutException.class);
    }

    private DummyExceptionTestDsl aHttpResponseWithCode(final int responseCode) {
        return new DummyExceptionTestDsl(responseCode);
    }

    private ExceptionTestDsl executing(final Supplier<TestSphereRequest> f) {
        return new ExceptionTestDsl(f);
    }


    private class DummyExceptionTestDsl {
        private final int responseCode;

        public DummyExceptionTestDsl(final int responseCode) {
            this.responseCode = responseCode;
        }

        public void resultsInA(final Class<? extends Throwable> type) throws Throwable {
            thrown.expect(type);
            try {
                SphereClientFactory.of()
                        .createHttpTestDouble(request -> HttpResponse.of(responseCode)).execute(CategoryQuery.of()).join();
            } catch (final CompletionException e) {
                throw e.getCause();
            }
        }
    }

    private class ExceptionTestDsl {
        private final Supplier<TestSphereRequest> f;

        public ExceptionTestDsl(final Supplier<TestSphereRequest> f) {
            this.f = f;
        }

        public void resultsInA(final Class<? extends Throwable> type) {
            thrown.expect(type);
            final TestSphereRequest testSphereRequest = f.get();
            execute(testSphereRequest);
        }
    }

    private static class TestSphereRequest extends Base implements SphereRequest<String> {

        private final HttpRequestIntent requestIntent;

        private TestSphereRequest(final HttpRequestIntent requestIntent) {
            this.requestIntent = requestIntent;
        }

        public static TestSphereRequest of(final HttpRequestIntent requestIntent) {
            return new TestSphereRequest(requestIntent);
        }


        @Override
        public Function<HttpResponse, String> resultMapper() {
            return null;
        }

        @Override
        public HttpRequestIntent httpRequestIntent() {
            return requestIntent;
        }
    }
}
