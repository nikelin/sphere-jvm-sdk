package io.sphere.sdk.test;

import io.sphere.sdk.client.*;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.exceptions.ConcurrentModificationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.ExecutionException;

public abstract class IntegrationTest {

    private static volatile TestClient client;
    private static volatile int threadCountAtStart;

    protected synchronized static TestClient client() {
        if (client == null) {
            final SphereClientFactory factory = SphereClientFactory.of();
            final SphereClientConfig config = SphereClientConfig.of(projectKey(), clientId(), clientSecret(), authUrl(), apiUrl());
            final SphereAccessTokenSupplier tokenSupplier = SphereAccessTokenSupplierFactory.of().createSupplierOfOneTimeFetchingToken(config);
            final SphereClient underlying = factory.createClient(config, tokenSupplier);
            client = new TestClient(underlying);
        }
        return client;
    }

    protected static String apiUrl() {
        return System.getenv("JVM_SDK_IT_SERVICE_URL");
    }

    protected static String authUrl() {
        return System.getenv("JVM_SDK_IT_AUTH_URL");
    }

    protected static String clientSecret() {
        return System.getenv("JVM_SDK_IT_CLIENT_SECRET");
    }

    protected static String clientId() {
        return System.getenv("JVM_SDK_IT_CLIENT_ID");
    }

    protected static String projectKey() {
        return System.getenv("JVM_SDK_IT_PROJECT_KEY");
    }

    protected static <T> T execute(final SphereRequest<T> sphereRequest) {
        try {
            return client().execute(sphereRequest);
        } catch (final TestClientException e) {
            if (e.getCause() instanceof ExecutionException && e.getCause().getCause() instanceof ConcurrentModificationException) {
                throw (ConcurrentModificationException) e.getCause().getCause();
            } else {
                throw e;
            }
        }
    }

    protected static void fail() {
        fail("this test should fail");
    }

    protected static void fail(final String message) {
        throw new RuntimeException(message);
    }

    @BeforeClass
    public synchronized static void setup() {
        threadCountAtStart = countThreads();
    }

    @AfterClass
    public synchronized static void shutdownClient() {
        if (client != null) {
            client.close();
            client = null;
            final int threadsNow = countThreads();
            if (threadsNow > threadCountAtStart) {
                throw new RuntimeException("Thread leak! After client shutdown created threads are still alive. Threads now: " + threadsNow + " Threads before: " + threadCountAtStart);
            }
        }
    }

    protected static int countThreads() {
        return Thread.activeCount();
    }
}
