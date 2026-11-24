package com.mxraven.mail.notify;

/**
 * A sampled routing-script fault from an MTA listener. The transaction was
 * deferred, so there is no accepted event for it.
 */
public final class FaultNotification implements NotificationEvent {
    private final FaultEvent fault;

    /**
     * Creates a fault notification.
     *
     * @param fault the fault details
     */
    public FaultNotification(FaultEvent fault) {
        this.fault = fault;
    }

    /**
     * Returns the fault details.
     *
     * @return the fault details
     */
    public FaultEvent fault() {
        return fault;
    }

    @Override
    public NotificationKind kind() {
        return NotificationKind.FAULT;
    }
}
