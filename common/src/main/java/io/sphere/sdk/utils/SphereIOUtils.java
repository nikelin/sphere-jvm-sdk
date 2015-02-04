package io.sphere.sdk.utils;

public final class SphereIOUtils {
    private SphereIOUtils() {
    }

    public static void closeQuietly(final AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final Exception e) {
            SphereInternalLogger.getLogger("io").error(() -> "Error on closing resource.", e);
        }
    }
}
