package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Resource types addressable by tenant resource search. */
public enum TenantSearchResourceType {
    DOMAIN("domain"),
    INBOUND_ROUTE("inbound_route"),
    LISTENER("listener"),
    ROUTING_RULE("routing_rule"),
    API_KEY("api_key"),
    SMTP_RELAY("smtp_relay"),
    STORAGE_INTEGRATION("storage_integration"),
    WEBHOOK_ENDPOINT("webhook_endpoint"),
    IDENTITY_PROVIDER("identity_provider"),
    AUTO_REPLY_TEMPLATE("auto_reply_template"),
    RECIPIENT_SET("recipient_set"),
    SUPPRESSION("suppression");

    private final String wire;

    TenantSearchResourceType(String wire) {
        this.wire = wire;
    }

    /** The {@code snake_case} value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static TenantSearchResourceType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (TenantSearchResourceType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown TenantSearchResourceType value: " + value);
    }
}
