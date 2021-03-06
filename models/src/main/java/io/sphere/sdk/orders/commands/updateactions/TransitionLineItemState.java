package io.sphere.sdk.orders.commands.updateactions;

import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.states.State;

import java.time.Instant;
import java.util.Optional;

public class TransitionLineItemState extends UpdateAction<Order> {

    private final String lineItemId;
    private final long quantity;
    private final Reference<State> fromState;
    private final Reference<State> toState;
    private final Optional<Instant> actualTransitionDate;


    private TransitionLineItemState(final String lineItemId, final long quantity, final Reference<State> fromState, final Reference<State> toState,
                                    final Optional<Instant> actualTransitionDate) {
        super("transitionLineItemState");
        this.lineItemId = lineItemId;
        this.quantity = quantity;
        this.fromState = fromState;
        this.toState = toState;
        this.actualTransitionDate = actualTransitionDate;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Reference<State> getFromState() {
        return fromState;
    }

    public Reference<State> getToState() {
        return toState;
    }

    public static TransitionLineItemState of(final String lineItemId, final long quantity,
                                             final Reference<State> fromState, final Reference<State> toState,
                                             final Optional<Instant> actualTransitionDate) {
        return new TransitionLineItemState(lineItemId, quantity, fromState, toState, actualTransitionDate);
    }

    public static UpdateAction<Order> of(final LineItem lineItem, final long quantity,
                                         final Reference<State> fromState, final Reference<State> toState,
                                         final Optional<Instant> actualTransitionDate) {
        return of(lineItem.getId(), quantity, fromState, toState, actualTransitionDate);
    }
}

