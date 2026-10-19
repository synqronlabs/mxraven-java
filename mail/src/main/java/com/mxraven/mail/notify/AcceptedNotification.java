package com.mxraven.mail.notify;

import com.mxraven.mail.webhook.InboundEmail;

import java.util.Objects;

/**
 * A message the listener accepted.
 */
public final class AcceptedNotification implements NotificationEvent {
    private final InboundEmail email;

    /**
     * The accepted-message payload, the same payload a
     * {@code DELIVER_WEBHOOK} delivery carries; download the raw message with
     * {@link InboundEmail#parse()} when needed.
     *
     * @return the accepted message
     */
    public InboundEmail email() {
        return email;
    }

    @Override
    public NotificationKind kind() {
        return NotificationKind.ACCEPTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcceptedNotification that = (AcceptedNotification) o;
        return Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }

    @Override
    public String toString() {
        return "AcceptedNotification[" + "email=" + this.email + "]";
    }

    /**
     * Creates a new AcceptedNotification.
     *
     * @param email the accepted message
     */
    public AcceptedNotification(InboundEmail email) {
        this.email = email;
    }
}
