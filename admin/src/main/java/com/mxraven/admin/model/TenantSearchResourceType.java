package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Resource types addressable by tenant resource search. */
public enum TenantSearchResourceType {
    /** A tenant domain. */
    DOMAIN("domain"),
    /** An inbound route. */
    INBOUND_ROUTE("inbound_route"),
    /** A listener. */
    LISTENER("listener"),
    /** A routing rule. */
    ROUTING_RULE("routing_rule"),
    /** An API key. */
    API_KEY("api_key"),
    /** An SMTP relay. */
    SMTP_RELAY("smtp_relay"),
    /** A storage integration. */
    STORAGE_INTEGRATION("storage_integration"),
    /** A webhook endpoint. */
    WEBHOOK_ENDPOINT("webhook_endpoint"),
    /** An identity provider. */
    IDENTITY_PROVIDER("identity_provider"),
    /** An auto-reply template. */
    AUTO_REPLY_TEMPLATE("auto_reply_template"),
    /** A recipient set. */
    RECIPIENT_SET("recipient_set"),
    /** A suppression entry. */
    SUPPRESSION("suppression");

    private final String wire;

    TenantSearchResourceType(String wire) {
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
