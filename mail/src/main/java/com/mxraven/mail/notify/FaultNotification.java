package com.mxraven.mail.notify;

/**
 * A sampled routing-script fault from an MTA listener. The transaction was
 * deferred, so there is no accepted event for it.
 *
 * @param fault the fault details
 */
public record FaultNotification(FaultEvent fault) implements NotificationEvent {

    @Override
    public NotificationKind kind() {
        return NotificationKind.FAULT;
    }
}
