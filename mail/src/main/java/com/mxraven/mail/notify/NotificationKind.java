package com.mxraven.mail.notify;

/** Identifies which kind of notification an event carries. */
public enum NotificationKind {
    /** A message the listener accepted and whose terminal action was resolved. */
    ACCEPTED("accepted"),
    /** One delivery attempt for an accepted message on an egress channel. */
    ATTEMPT("attempt");

    private final String wire;

    NotificationKind(String wire) {
        this.wire = wire;
    }

    /**
     * Returns the value used in the notification subject.
     *
     * @return the wire value
     */
    public String wire() {
        return wire;
    }
}
