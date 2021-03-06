package io.sphere.sdk.projects.queries;

import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.client.SphereRequestBase;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.json.JsonUtils;

import java.util.function.Function;

import static io.sphere.sdk.http.HttpMethod.GET;

public class ProjectGet extends SphereRequestBase implements SphereRequest<Project> {
    private ProjectGet() {
    }

    @Override
    public Function<HttpResponse, Project> resultMapper() {
        return httpResponse -> JsonUtils.readObject(Project.typeReference(), httpResponse.getResponseBody().get());
    }

    @Override
    public HttpRequestIntent httpRequestIntent() {
        return HttpRequestIntent.of(GET, "");
    }

    public static SphereRequest<Project> of() {
        return new ProjectGet();
    }
}
