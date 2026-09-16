package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Action a routing rule applies. */
public enum RoutingRuleActionKind {
    /** Delivers through a dedicated IP pool. */
    DELIVER_DEDICATED("DELIVER_DEDICATED"),
    /** Delivers normally. */
    DELIVER("DELIVER"),
    /** Relays through a smart host. */
    SMARTHOST_RELAY("SMARTHOST_RELAY"),
    /** Drops the message. */
    DROP("DROP"),
    /** Rejects the message with an SMTP error. */
    REJECT("REJECT"),
    /** Modifies message headers. */
    MODIFY_HEADER("MODIFY_HEADER"),
    /** Adds recipients. */
    ADD_RECIPIENT("ADD_RECIPIENT"),
    /** Notifies a webhook. */
    NOTIFY_WEBHOOK("NOTIFY_WEBHOOK"),
    /** Delivers to a webhook. */
    DELIVER_WEBHOOK("DELIVER_WEBHOOK"),
    /** Forwards over SMTP. */
    SMTP_FORWARD("SMTP_FORWARD"),
    /** Relays to a relay. */
    RELAY("RELAY"),
    /** Stores the message in S3. */
    S3_STORE("S3_STORE"),
    /** Sends an auto-reply. */
    AUTO_REPLY("AUTO_REPLY");

    private final String wire;

    RoutingRuleActionKind(String wire) {
        this.wire = wire;
    }

    /**
     * The {@code snake_case} value used on the wire.
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
    public static RoutingRuleActionKind fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (RoutingRuleActionKind candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown RoutingRuleActionKind value: " + value);
    }
}
