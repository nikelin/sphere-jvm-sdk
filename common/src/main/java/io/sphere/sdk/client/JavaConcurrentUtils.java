package io.sphere.sdk.client;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//TODO delete
final class JavaConcurrentUtils {
    private JavaConcurrentUtils() {
    }

    /** Creates a thread factory.
     *  @param poolName Name of created threads (an auto-incrementing suffix is appended).
     *
     *  @return a new ThreadFactory
     */
    public static ThreadFactory namedThreadFactory(final String poolName) {
        return new ThreadFactory() {
            final AtomicInteger count = new AtomicInteger(0);
            @Override public Thread newThread(Runnable r) {
                return new Thread(r, poolName + "-" + count.incrementAndGet());
            }
        };
    }

    /** Creates an executor that only allows execution of a single task at a time.
     *
     * @param poolName the name of the pool
     * @return a new ThreadPoolExecutor
     */
    public static ThreadPoolExecutor singleTaskExecutor(final String poolName) {
        return new ThreadPoolExecutor(
                1, 1, 30, TimeUnit.SECONDS, new SynchronousQueue<>(), namedThreadFactory(poolName));
    }
}
