package io.sphere.sdk.orders.commands.updateactions;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.ReturnItem;
import io.sphere.sdk.orders.ReturnShipmentState;

/**

 {@include.example io.sphere.sdk.orders.commands.OrderUpdateCommandTest#setReturnShipmentState()}
 */
public class SetReturnShipmentState extends UpdateAction<Order> {
    private final String returnItemId;
    private final ReturnShipmentState shipmentState;

    private SetReturnShipmentState(final String returnItemId, final ReturnShipmentState shipmentState) {
        super("setReturnShipmentState");
        this.returnItemId = returnItemId;
        this.shipmentState = shipmentState;
    }

    public String getReturnItemId() {
        return returnItemId;
    }

    public ReturnShipmentState getShipmentState() {
        return shipmentState;
    }

    public static SetReturnShipmentState of(final String returnItemId, final ReturnShipmentState shipmentState) {
        return new SetReturnShipmentState(returnItemId, shipmentState);
    }

    public static SetReturnShipmentState of(final ReturnItem returnItem, final ReturnShipmentState shipmentState) {
        return of(returnItem.getId(), shipmentState);
    }
}
