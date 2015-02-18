package io.sphere.sdk.client;


import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.fest.assertions.Assertions.assertThat;

public class AuthActorTest {

    private static final CompletableFuture<Tokens> TOKENS_FUTURE = CompletableFutureUtils.successful(Tokens.of("ac", "re", Optional.of(50L)));
    private static final CompletableFuture<Tokens> FAILED_FUTURE = CompletableFutureUtils.failed(new RuntimeException());

    private static void withActor(final AuthActor actor, final Consumer<Actor> consumer) {
        try {
            consumer.accept(actor);
        } finally {
            actor.close();
        }
    }
//
//    @Test
//    public void fetchesToken() throws Exception {
//        final TokensSupplier tokensSupplier = new TestTokensSupplier(TOKENS_FUTURE);
//        final AccessTokenCallback1 tokenCallback = new AccessTokenCallback1();
//        withActor(new AuthActor(tokensSupplier), authActor -> {
//            authActor.tell(new AuthActorProtocol.FetchTokenFromSphereMessage());
//            wait(tokenCallback);
//            assertThat(tokenCallback.isSuccessful).isTrue();
//        });
//    }
//
//    @Test
//    public void nothingHappensWithoutTell() throws Exception {
//        final TokensSupplier tokensSupplier = new TestTokensSupplier(TOKENS_FUTURE);
//        final AccessTokenCallback1 tokenCallback = new AccessTokenCallback1();
//        withActor(new AuthActor(tokensSupplier), authActor -> {
//            wait(tokenCallback);
//            assertThat(tokenCallback.isSuccessful).isFalse();
//        });
//    }
//
//    @Test
//    public void refetchOnError() throws Exception {
//        final AccessTokenCallback1 tokenCallback = new AccessTokenCallback1();
//        final TokensSupplier tokensSupplier = new TokensSupplier() {
//            boolean firstTime = true;
//
//            @Override
//            public CompletableFuture<Tokens> get() {
//                final CompletableFuture<Tokens> result = firstTime ? FAILED_FUTURE : TOKENS_FUTURE;
//                firstTime = false;
//                return result;
//            }
//
//            @Override
//            public void close() {
//
//            }
//        };
//        withActor(new AuthActor(tokensSupplier, tokenCallback), authActor -> {
//            authActor.tell(new AuthActorProtocol.FetchTokenFromSphereMessage());
//            wait(tokenCallback, 100);
//            assertThat(tokenCallback.isSuccessful).isTrue();
//        });
//    }
//
//    private static class TestTokensSupplier implements TokensSupplier {
//
//        private CompletableFuture<Tokens> future;
//
//        public TestTokensSupplier(final CompletableFuture<Tokens> future) {
//            this.future = future;
//        }
//
//        @Override
//        public CompletableFuture<Tokens> get() {
//            return future;
//        }
//
//        @Override
//        public void close() {
//
//        }
//    }
}