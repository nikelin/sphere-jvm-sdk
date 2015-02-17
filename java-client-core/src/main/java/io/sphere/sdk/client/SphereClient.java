package io.sphere.sdk.client;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

/**
 * A client interface to perform requests to SPHERE.IO.
 *
 * <h3 id=instantiation>Instantiation</h3>
 *
 * {@include.example example.JavaClientInstantiationExample}
 *
 * <h3 id=example-call>Example call</h3>
 *
 * {@include.example example.TaxCategoryQueryExample#exampleQuery()}
 *
 * Refer to <a href="../meta/SphereResources.html">resources</a> for known SPHERE.IO requests.
 *
 */
public interface SphereClient extends Closeable {
    /**
     * Performs one request to the SPHERE.IO API, this includes the sending the HTTP request and transform the resulting JSON into a Java object.
     *
     * <p>This method should not throw exceptions, the exceptions should be in the {@link java.util.concurrent.CompletableFuture}.</p>
     * The method is thread-safe.
     *
     * @param sphereRequest the description of the request and the response transformation
     * @param <T> the type of the resulting Java object if no exception occurs
     * @return the result embedded into a future, the result can be the object or an exception
     */
    <T> CompletableFuture<T> execute(final SphereRequest<T> sphereRequest);

    void close();
}
