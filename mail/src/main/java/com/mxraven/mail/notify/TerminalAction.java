package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The final routing action a listener's script resolved.
 *
 * <p>Values are the mxRaven worker enum names.
 */
public enum TerminalAction {
    /** The server did not record a terminal action. */
    UNSPECIFIED("TERMINAL_ACTION_TYPE_UNSPECIFIED"),
    /** Delivers through a dedicated IP pool. */
    DELIVER_DEDICATED("TERMINAL_ACTION_TYPE_DELIVER_DEDICATED"),
    /** Relays through the configured smarthost. */
    SMARTHOST_RELAY("TERMINAL_ACTION_TYPE_SMARTHOST_RELAY"),
    /**
     * Delivered to a webhook endpoint.
     *
     * @deprecated DELIVER_WEBHOOK was removed with webhook delivery. The value
     *             is retained because the wire enum keeps it for compatibility.
     */
    @Deprecated
    DELIVER_WEBHOOK("TERMINAL_ACTION_TYPE_DELIVER_WEBHOOK"),
    /** Forwards the message to an SMTP destination. */
    SMTP_FORWARD("TERMINAL_ACTION_TYPE_SMTP_FORWARD"),
    /** Relays the message to another MX. */
    RELAY("TERMINAL_ACTION_TYPE_RELAY"),
    /** Stores the message in object storage. */
    S3_STORE("TERMINAL_ACTION_TYPE_S3_STORE"),
    /** Sends an automatic reply. */
    AUTO_REPLY("TERMINAL_ACTION_TYPE_AUTO_REPLY"),
    /** Accepts and discards the message. */
    DROP("TERMINAL_ACTION_TYPE_DROP"),
    /** Rejects the message. */
    REJECT("TERMINAL_ACTION_TYPE_REJECT"),
    /** Delivers the message normally. */
    DELIVER("TERMINAL_ACTION_TYPE_DELIVER"),
    /** Handles a DSN at an SRS return address. */
    SRS_RETURN("TERMINAL_ACTION_TYPE_SRS_RETURN"),
    /** Generates a local DSN. */
    LOCAL_DSN("TERMINAL_ACTION_TYPE_LOCAL_DSN");

    private final String wire;

    TerminalAction(String wire) {
        this.wire = wire;
    }

    /**
     * The value used on the wire.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the constant for a wire value.
     *
     * @param value wire value, or {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if the value is unknown
     */
    @JsonCreator
    public static TerminalAction fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (TerminalAction candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown TerminalAction value: " + value);
    }
}
