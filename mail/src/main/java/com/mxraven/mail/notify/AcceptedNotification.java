package com.mxraven.mail.notify;

import com.mxraven.mail.webhook.InboundEmail;

/**
 * A message the listener accepted.
 *
 * @param email the accepted-message payload, the same payload a
 *              {@code DELIVER_WEBHOOK} delivery carries; download the raw
 *              message with {@link InboundEmail#parse()} when needed
 */
public record AcceptedNotification(InboundEmail email) implements NotificationEvent {

    @Override
    public NotificationKind kind() {
        return NotificationKind.ACCEPTED;
    }
}
