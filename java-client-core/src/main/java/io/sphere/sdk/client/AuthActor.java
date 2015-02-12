package io.sphere.sdk.client;

import io.sphere.sdk.models.Base;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.sphere.sdk.client.CompletableFutureUtils.transferResult;
import static io.sphere.sdk.utils.SphereIOUtils.closeQuietly;
import static io.sphere.sdk.client.SphereAuth.*;

final class AuthActor extends Actor {
    private final TokensSupplier internalTokensSupplier;
    private final AutoRefreshSphereAccessTokenSupplierImpl autoRefreshSphereAccessTokenSupplier;
    private Optional<TokensHolder> tokensCache = Optional.empty();
    private final List<TokenRequestMessage> unansweredRequests = new LinkedList<>();

    public AuthActor(final TokensSupplier internalTokensSupplier, final AutoRefreshSphereAccessTokenSupplierImpl autoRefreshSphereAccessTokenSupplier) {
        this.internalTokensSupplier = internalTokensSupplier;
        this.autoRefreshSphereAccessTokenSupplier = autoRefreshSphereAccessTokenSupplier;
    }

    @Override
    protected void receive(final Object message) {
        receiveBuilder(message)
                .when(TokenRequestMessage.class, m -> {

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
                    unansweredRequests.forEach(trm -> transferResult(tokensHolder.tokenFuture, trm.tokenFuture));
                    unansweredRequests.clear();
                    autoRefreshSphereAccessTokenSupplier.setToken(sm.tokens.getAccessToken());

                    //TODO trigger next fetch!!!
                    //TODO handle failures
                })
                .when(TokenRequestMessage.class, tkr -> {
                    if (tokensCache.isPresent()) {
                        transferResult(tokensCache.get().tokenFuture, tkr.tokenFuture);
                    } else {
                        unansweredRequests.add(tkr);
                    }
                })
                .when(ErroredTokenFetchMessage.class, er -> {
                    AUTH_LOGGER.error(() -> "Can't fetch tokens.", er.cause);
                });
    }


    @Override
    protected void closeInternal() {
        closeQuietly(internalTokensSupplier);
    }

    public static class TokenRequestMessage extends Base {
        final CompletableFuture<String> tokenFuture;

        public TokenRequestMessage(final CompletableFuture<String> tokenFuture) {
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
    public static class FetchTokenMessage extends Base {
        public FetchTokenMessage() {
        }
    }

    public static class TokensHolder extends Base {
        final Tokens tokens;
        final CompletableFuture<String> tokenFuture;

        public TokensHolder(final Tokens tokens) {
            this.tokens = tokens;
            this.tokenFuture = CompletableFuture.completedFuture(tokens.getAccessToken());
        }
    }

    private static class NeverTriedException extends RuntimeException {
        static final long serialVersionUID = 0L;
    }
}
