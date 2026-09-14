package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The final routing action recorded for an inbound message. Values are the
 * mxRaven worker enum names; compare against these constants instead of
 * literals.
 */
public enum TerminalAction {
    /** Delivers through a dedicated IP pool. */
    DELIVER_DEDICATED("TERMINAL_ACTION_TYPE_DELIVER_DEDICATED"),
    /** Relays through the configured smarthost. */
    SMARTHOST_RELAY("TERMINAL_ACTION_TYPE_SMARTHOST_RELAY"),
    /** Delivers to a webhook endpoint. */
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

    /** The mxRaven worker enum name used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
        throw new IllegalArgumentException("unknown terminal action: " + value);
    }
}
