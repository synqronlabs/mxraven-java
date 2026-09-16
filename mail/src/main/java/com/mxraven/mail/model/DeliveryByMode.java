package com.mxraven.mail.model;

/**
 * The DELIVERBY notification mode (RFC 2852).
 */
public enum DeliveryByMode {
    /** Notify the sender on failure or expiry ({@code N}). */
    NOTIFY("N"),
    /** Return the message on failure or expiry ({@code R}). */
    RETURN("R");

    private final String wire;

    DeliveryByMode(String wire) {
        this.wire = wire;
    }

    /**
     * The keyword used in the DELIVERBY parameter.
     *
     * @return the wire keyword
     */
    public String wire() {
        return wire;
    }
}
