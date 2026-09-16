package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Webhook delivery kind. */
public enum WebhookDeliveryKind {
    /** A delivery webhook. */
    DELIVER_WEBHOOK("deliver_webhook"),
    /** A notification webhook. */
    NOTIFY_WEBHOOK("notify_webhook");

    private final String wire;

    WebhookDeliveryKind(String wire) {
        this.wire = wire;
    }

    /**
     * The wire value.
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
    public static WebhookDeliveryKind fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (WebhookDeliveryKind candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown WebhookDeliveryKind value: " + value);
    }
}
