package io.sphere.sdk.utils;

import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.substringBefore;

/** Internal logging used by the sphere Java client itself.
 *
 *  <p>Uses slf4j logger named 'sphere' and does not depend on Play logger
 *  (which also uses slf4j loggers, named 'play' and 'application').</p>
 *
 *  <p>The logger can be configured per Play application using 'logger.sphere' in 'application.conf'.</p>
 *
 *  {@include.example example.LoggingExample}
 *
 *  <h3 id="logger-hierarchy">Logger hierarchy</h3>
 *
 *  <p>The loggers form a hierarchy separated by a dot. The root logger is {@code "sphere"} which is implicit set for
 *  {@link SphereInternalLogger#getLogger(java.lang.String)}, so you never include "sphere" in the logger name.</p>
 *  <p>The child loggers of sphere are the endpoints, so for example {@code "sphere.categories"} for categories and
 *  {@code "sphere.product-types"} for product types.</p>
 *  <p>The grandchild loggers refer to the action. {@code "sphere.categories.requests"} refers to
 *  performing requests per HTTPS to SPHERE.IO for categories, {@code "sphere.categories.responses"} refers to the responses of SPHERE.IO.
 *  {@code "sphere.categories.objects"} is for non HTTP API stuff like local object creation.
 *  </p>
 *
 *  The logger makes use of different log levels, so for example {@code "sphere.categories.responses"} logs on debug level the
 *  http response from SPHERE.io (abbreviated example):
 *
 *  <pre>10:59:18.623 [ForkJoinPool.commonPool-worker-3] DEBUG sphere.categories.responses - io.sphere.sdk.requests.HttpResponse@39984ae7[statusCode=200,responseBody={"offset":0,"count":4,"total":4,"results":[{"id":"2c41b33e-2d8e-415c-a4b7-f62628fa06e3","version":1,"name":{"en":"Hats"}, [...]</pre>
 *
 * {@code "sphere.categories.responses"} logs on trace level additional the formatted http response from SPHERE.io (abbreviated example):
 *
 * <pre>10:59:18.657 [ForkJoinPool.commonPool-worker-3] TRACE sphere.categories.responses - 200
 {
     "offset" : 0,
     "count" : 4,
     "total" : 4,
     "results" : [ {
         "id" : "2c41b33e-2d8e-415c-a4b7-f62628fa06e3",
         "version" : 1,
         "name" : {
            "en" : "Hats"
         },
         "slug" : {
            "en" : "hats"
         },
         "ancestors" : [ ],
         "orderHint" : "0.000013999891309781676091866",
         "createdAt" : "2014-05-13T13:52:10.978Z",
         "lastModifiedAt" : "2014-05-13T13:52:10.978Z"
         },
[...]</pre>

 {@code sphere.products.responses.queries} logs only HTTP GET requests and {@code sphere.products.responses.commands}
 logs only HTTP POST/DELETE requests.

 <h3 id="ning-logger">Remove the chatty output of the Ning HTTP client</h3>
 The current Java client uses internally ning and it logs by default a lot, so you need to set the loglevel in your log configuration.
 For logback with {@code logback.xml} or {@code logback-test.xml} the setting is {@code <logger name="com.ning.http.client" level="WARN"/>}.
 */
public final class SphereInternalLogger {
    private final Logger underlyingLogger;

    private SphereInternalLogger(final Logger underlyingLogger) {
        this.underlyingLogger = underlyingLogger;
    }

    public SphereInternalLogger debug(final Supplier<Object> message) {
        if (underlyingLogger.isDebugEnabled()) {
            underlyingLogger.debug(message.get().toString());
        }
        return this;
    }

    public SphereInternalLogger trace(final Supplier<Object> message) {
        if (isTraceEnabled()) {
            underlyingLogger.trace(message.get().toString());
        }
        return this;
    }

    public boolean isTraceEnabled() {
        return underlyingLogger.isTraceEnabled();
    }

    public SphereInternalLogger warn(final Supplier<Object> message) {
        if (underlyingLogger.isWarnEnabled()) {
            underlyingLogger.warn(message.get().toString());
        }
        return this;
    }

    public SphereInternalLogger error(final Supplier<Object> message, final Throwable throwable) {
        if (underlyingLogger.isErrorEnabled()) {
            underlyingLogger.error(message.get().toString(), throwable);
        }
        return this;
    }

    public static SphereInternalLogger getLogger(final HttpRequest httpRequest) {
        final HttpMethod httpMethod = httpRequest.getHttpMethod();
        return getLogger(getPathElement(httpRequest) + ".requests." + requestOrCommandScopeSegment(httpMethod));
    }

    public static SphereInternalLogger getLogger(final HttpResponse response) {
        final String firstPathElement = response.getAssociatedRequest()
                .map(r -> getPathElement(r)).orElse("endpoint-unknown");
        final String lastPathElement = response.getAssociatedRequest()
                .map(r -> requestOrCommandScopeSegment(r.getHttpMethod())).orElse("execution-type-unknown");
        return getLogger(firstPathElement + ".responses." + lastPathElement);
    }

    public static SphereInternalLogger getLogger(final Class<?> clazz) {
        return new SphereInternalLogger(LoggerFactory.getLogger(clazz));
    }

    public static SphereInternalLogger getLogger(final String loggerName) {
        return new SphereInternalLogger(LoggerFactory.getLogger("sphere." + loggerName));
    }

    private static String getPathElement(final HttpRequest httpRequest) {
        final String path = httpRequest.getUrl();
        return path.split("/")[4];
    }

    private static String requestOrCommandScopeSegment(final HttpMethod httpMethod) {
        return httpMethod == HttpMethod.GET ? "queries" : "commands";
    }
}