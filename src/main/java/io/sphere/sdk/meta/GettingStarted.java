package io.sphere.sdk.meta;

import io.sphere.sdk.models.Base;

/**
 <h3 id=about-clients>About the clients</h3>
 <p>The SPHERE.IO client communicates asynchronously with the SPHERE.IO API via HTTPS and takes care about authentication.</p>
 <p>The client uses Java objects to formulate a HTTP request, performs the request and maps the response JSON into a Java object.
 The resulting Java object is not directly accessible as object, it is embedded in a Future/Promise for asynchronous programming.
 Since the client is thread-safe you need only one client to perform multiple requests in parallel.</p>

 <p>There are different SPHERE.IO client flavors for different future implementations:</p>

 <table>
 <caption>Clients and future implementations</caption>
 <tr><th>Client</th><th>Future implementation</th></tr>
 <tr><td>{@link io.sphere.sdk.client.SphereClient} (default)</td><td>{@code java.util.concurrent.CompletableFuture}</td></tr>
 <tr><td><a href=https://github.com/sphereio/sphere-jvm-sdk-scala-add-ons>SphereScalaClient</a></td><td>{@code scala.concurrent.Future}</td></tr>
 <tr><td><a href=https://github.com/sphereio/sphere-jvm-sdk-scala-add-ons>SpherePlayJavaClient</a></td><td>{@code play.libs.F.Promise}</td></tr>
 </table>

 <h3 id=preparation>Preparation</h3>

 <p>You need to <a href="https://admin.sphere.io">create a project in SPHERE.IO</a>.
 Then you need to go to the "Developers" section and open the tab "API clients".
 There you find the credentials to access the project with the client.</p>

 <p>This is an example for the client credentials page (client secret has been modified):</p>

 <img src="{@docRoot}/documentation-resources/images/merchant-center/project-credentials.png" alt="Merchant Center with project credentials view">

<p>For this example the configuration values are:</p>

 <pre><code>project key: jvm-sdk-dev-1
client ID: ELqF0rykXD2fyS8s-IhIPKfQ
client secret: 222222222222222222222222222222226</code></pre>

 <h3 id=instantiation>Instantiation</h3>

 {@include.example example.JavaClientInstantiationExample}

 <h3 id=perform-requests>Perform requests</h3>

 <p>A client works on the abstraction level of one HTTP request for one project.
 With one client you can start multiple requests in parallel, it is thread-safe.</p>
 <p>The clients have a method {@link io.sphere.sdk.client.SphereClient#execute(io.sphere.sdk.client.SphereRequest)}, which takes a {@link io.sphere.sdk.client.SphereRequest} as parameter.</p>

 <p>You can create {@link io.sphere.sdk.client.SphereRequest} yourself or use the given ones which are listed on {@link io.sphere.sdk.meta.SphereResources}.</p>
 <p>Example:</p>

 {@include.example example.TaxCategoryQueryExample#exampleQuery()}

 <h3 id=closing>Closing the client</h3>

 The client holds resources like thread pools and IO connections, so call {@link io.sphere.sdk.client.SphereClient#close()} to release them.

 <h3 id=further-client-infos>Further client information</h3>
 <ul>
  <li>{@link SphereClientTuningDocumentation Tuning the client}</li>
  <li>{@link io.sphere.sdk.meta.TestingDocumentation Writing unit tests with the client}</li>
 </ul>
 */
public final class GettingStarted extends Base {
    private GettingStarted() {
    }
}
