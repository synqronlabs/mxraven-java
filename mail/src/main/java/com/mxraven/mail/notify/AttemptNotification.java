package com.mxraven.mail.notify;

/**
 * One delivery attempt for an accepted message.
 *
 * @param attempt the attempt details
 */
public record AttemptNotification(AttemptEvent attempt) implements NotificationEvent {

    @Override
    public NotificationKind kind() {
        return NotificationKind.ATTEMPT;
    }
}
