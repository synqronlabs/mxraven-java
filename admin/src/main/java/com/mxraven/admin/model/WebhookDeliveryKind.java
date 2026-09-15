package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Webhook delivery kind. */
public enum WebhookDeliveryKind {
    DELIVER_WEBHOOK("deliver_webhook"),
    NOTIFY_WEBHOOK("notify_webhook");

    private final String wire;

    WebhookDeliveryKind(String wire) {
        this.wire = wire;
    }

    /** The wire value. */
    @JsonValue
    public String wire() {
        return wire;
    }

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
