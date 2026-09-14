package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Identifies the shape of a decoded webhook payload.
 */
public enum WebhookEventType {
    /** A {@code DELIVER_WEBHOOK} delivery carrying a complete inbound message. */
    INBOUND_EMAIL("inbound_email"),
    /** A {@code NOTIFY_WEBHOOK} delivery carrying the status of an object-storage write. */
    STORAGE_STATUS("s3_egress_status"),
    /**
     * A {@code NOTIFY_WEBHOOK} delivery carrying the status of an SMTP delivery
     * attempt. The SMTP producer sends no {@code event_type} field, so this
     * value is assigned while decoding.
     */
    DELIVERY_STATUS("delivery_status");

    private final String wire;

    WebhookEventType(String wire) {
        this.wire = wire;
    }

    /** The value used on the wire. */
    @JsonValue
    public String wire() {
        return wire;
    }

    @JsonCreator
    public static WebhookEventType fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (WebhookEventType candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown webhook event type: " + value);
    }
}
