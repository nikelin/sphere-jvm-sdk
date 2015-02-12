package io.sphere.sdk.client;

import io.sphere.sdk.exceptions.InvalidClientCredentialsException;
import io.sphere.sdk.models.Base;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.CompletableFutureUtils.transferResult;
import static io.sphere.sdk.utils.SphereIOUtils.closeQuietly;
import static io.sphere.sdk.client.SphereAuth.*;

final class AuthActor extends Actor {
    private final TokensSupplier internalTokensSupplier;
    private final AccessTokenCallback accessTokenCallback;
    private Optional<TokensHolder> tokensCache = Optional.empty();
    private final List<TokenIsRequestedMessage> unansweredRequests = new LinkedList<>();

    public AuthActor(final TokensSupplier internalTokensSupplier, final AccessTokenCallback accessTokenCallback) {
        this.internalTokensSupplier = internalTokensSupplier;
        this.accessTokenCallback = accessTokenCallback;
    }

    @Override
    protected void receive(final Object message) {
        receiveBuilder(message)
                .when(FetchTokenFromSphereMessage.class, m -> {
                    final CompletableFuture<Tokens> tokensFuture = internalTokensSupplier.get();
                    tokensFuture.whenCompleteAsync((r, throwable) -> {
                        if (throwable == null) {
                            tell(new SuccessfulTokenFetchMessage(r));
                        } else {
                            tell(new ErroredTokenFetchMessage(throwable));
                        }
                    });
                })
                .when(SuccessfulTokenFetchMessage.class, sm -> {
                    final TokensHolder tokensHolder = new TokensHolder(sm.tokens);
                    tokensCache = Optional.of(tokensHolder);
                    //a solution would be to append the unanswered lists to the mailbox
                    //but then the order of the executions becomes weird because old
                    //requests are started later
                    //this is dangerous also because of timeouts
                    completeUnansweredRequests(tokensHolder.tokenFuture);
                    accessTokenCallback.setToken(sm.tokens.getAccessToken());
                    final Long delayInSecondsToFetchNewToken = tokensHolder.tokens.getExpiresIn().map(ttlInSeconds -> ttlInSeconds - 60 * 5).orElse(60 * 30L);
                    schedule(new FetchTokenFromSphereMessage(), delayInSecondsToFetchNewToken, TimeUnit.SECONDS);
                })
                .when(TokenIsRequestedMessage.class, tkr -> {
                    if (tokensCache.isPresent()) {
                        transferResult(tokensCache.get().tokenFuture, tkr.tokenFuture);
                    } else {
                        unansweredRequests.add(tkr);
                    }
                })
                .when(ErroredTokenFetchMessage.class, er -> {
                    AUTH_LOGGER.error(() -> "Can't fetch tokens.", er.cause);
                    if (er.cause instanceof InvalidClientCredentialsException) {
                        tokensCache = Optional.of(tokensCache.orElseGet(() -> new TokensHolder(er.cause)));
                    } else {
                        tell(new FetchTokenFromSphereMessage());
                    }
                    completeUnansweredRequests(CompletableFutureUtils.failed(er.cause));
                });
    }

    private void completeUnansweredRequests(final CompletableFuture<String> tokenFuture) {
        unansweredRequests.forEach(trm -> transferResult(tokenFuture, trm.tokenFuture));
        unansweredRequests.clear();
    }


    @Override
    protected void closeInternal() {
        closeQuietly(internalTokensSupplier);
    }

    public static class TokenIsRequestedMessage extends Base {
        final CompletableFuture<String> tokenFuture;

        public TokenIsRequestedMessage(final CompletableFuture<String> tokenFuture) {
            this.tokenFuture = tokenFuture;
        }
    }

    public static class ErroredTokenFetchMessage extends Base {
        final Throwable cause;

        public ErroredTokenFetchMessage(final Throwable cause) {
            this.cause = cause;
        }
    }

    public static class SuccessfulTokenFetchMessage extends Base {
        final Tokens tokens;

        public SuccessfulTokenFetchMessage(final Tokens tokens) {
            this.tokens = tokens;
        }
    }
    public static class FetchTokenFromSphereMessage extends Base {
        public FetchTokenFromSphereMessage() {
        }
    }

    public static class TokensHolder extends Base {
        final Tokens tokens;
        final CompletableFuture<String> tokenFuture;

        public TokensHolder(final Throwable error) {
            tokenFuture = CompletableFutureUtils.failed(error);
            tokens = null;
        }

        public TokensHolder(final Tokens tokens) {
            this.tokens = tokens;
            this.tokenFuture = CompletableFuture.completedFuture(tokens.getAccessToken());
        }
    }

    private static class NeverTriedException extends RuntimeException {
        static final long serialVersionUID = 0L;
    }
}
