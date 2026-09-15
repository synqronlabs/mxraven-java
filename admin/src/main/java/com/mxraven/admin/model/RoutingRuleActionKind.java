package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Action a routing rule applies. */
public enum RoutingRuleActionKind {
    DELIVER_DEDICATED("DELIVER_DEDICATED"),
    DELIVER("DELIVER"),
    SMARTHOST_RELAY("SMARTHOST_RELAY"),
    DROP("DROP"),
    REJECT("REJECT"),
    MODIFY_HEADER("MODIFY_HEADER"),
    ADD_RECIPIENT("ADD_RECIPIENT"),
    NOTIFY_WEBHOOK("NOTIFY_WEBHOOK"),
    DELIVER_WEBHOOK("DELIVER_WEBHOOK"),
    SMTP_FORWARD("SMTP_FORWARD"),
    RELAY("RELAY"),
    S3_STORE("S3_STORE"),
    AUTO_REPLY("AUTO_REPLY");

    private final String wire;

    RoutingRuleActionKind(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
