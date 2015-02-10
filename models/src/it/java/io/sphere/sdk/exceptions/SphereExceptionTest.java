package io.sphere.sdk.exceptions;

import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.http.HttpRequestIntent;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;
import java.util.function.Supplier;

import static io.sphere.sdk.http.HttpMethod.POST;

public class SphereExceptionTest extends IntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidJsonInHttpRequestIntent() throws Exception {
        executing(() -> TestSphereRequest.of(HttpRequestIntent.of(POST, "/categories", "{invalidJson :)")))
                .resultsInA(JsonException.class);
    }

    private ExceptionTestDsl executing(final Supplier<TestSphereRequest> f) {
        return new ExceptionTestDsl(f);
    }


    private class ExceptionTestDsl {
        private final Supplier<TestSphereRequest> f;

        public ExceptionTestDsl(final Supplier<TestSphereRequest> f) {
            this.f = f;
        }

        public void resultsInA(final Class<? extends Throwable> type) {
            thrown.expect(JsonException.class);
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
