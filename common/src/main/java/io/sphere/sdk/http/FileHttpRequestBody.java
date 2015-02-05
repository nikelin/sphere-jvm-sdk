package io.sphere.sdk.http;

import io.sphere.sdk.models.Base;

import java.io.File;

public class FileHttpRequestBody extends Base implements HttpRequestBody {
    private final File body;

    private FileHttpRequestBody(final File body) {
        this.body = body;
    }

    public static FileHttpRequestBody of(final File body) {
        return new FileHttpRequestBody(body);
    }

    public File getUnderlying() {
        return body;
    }
}
