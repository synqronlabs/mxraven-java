package com.mxraven.mail.notify;

import java.util.Objects;

/**
 * One delivery attempt for an accepted message.
 */
public final class AttemptNotification implements NotificationEvent {
    private final AttemptEvent attempt;

    /**
     * The attempt details.
     *
     * @return the attempt event
     */
    public AttemptEvent attempt() {
        return attempt;
    }

    @Override
    public NotificationKind kind() {
        return NotificationKind.ATTEMPT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttemptNotification that = (AttemptNotification) o;
        return Objects.equals(this.attempt, that.attempt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.attempt);
    }

    @Override
    public String toString() {
        return "AttemptNotification[" + "attempt=" + this.attempt + "]";
    }

    /**
     * Creates a new AttemptNotification.
     *
     * @param attempt the attempt details
     */
    public AttemptNotification(AttemptEvent attempt) {
        this.attempt = attempt;
    }
}
